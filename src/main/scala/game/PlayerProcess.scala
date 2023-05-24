package game

import cats.data.EitherT
import cats.effect.{Async, Ref, Sync}
import cats.implicits._
import domain.ID._
import domain._
import domain.GameError._
import domain.PlayerRole._
import service.db.PlayerTableDbService

trait PlayerProcess[F[_]] {
  def createGame(playerName: String): F[(GameId, PlayerId, TeamId, TeamId)]
  def createPlayer(name: String, gameId: GameId, teamId: TeamId): F[PlayerId]
  def getPlayer(playerId: PlayerId): ErrorOrT[F, Player]
  def changeTeam(playerId: PlayerId, newTeam: TeamId): ErrorOrT[F, Unit]
  def changeRole(playerId: PlayerId, teamId: TeamId, newRole: PlayerRole): ErrorOrT[F, Unit]
  def startGame(playerId: PlayerId): ErrorOrT[F, Unit]
  def finishTurn(playerId: PlayerId): ErrorOrT[F, Unit]
  def playCard(playerId: PlayerId, cardId: CardId): ErrorOrT[F, Unit]
}
object PlayerProcess {
  def apply[F[_]: Async](playerTableService: PlayerTableDbService[F]
                        , gameProcess: GameProcess[F]
                        , teamProcess: TeamProcess[F]
                        , cardProcess: CardProcess[F]): PlayerProcess[F] =
    new PlayerProcess[F] {
      override def createGame(playerName: String): F[(GameId, PlayerId, TeamId, TeamId)] = {
        for {
          gameId     <- gameProcess.createGame()
          redTeamId  <- teamProcess.createTeam(gameId, TeamColor.Red)
          blueTeamId <- teamProcess.createTeam(gameId, TeamColor.Blue)
          playerId   <- createPlayer(playerName, gameId, redTeamId)
        } yield (gameId, playerId, redTeamId, blueTeamId)
      }

      override def createPlayer(name: String, gameId: GameId, teamId: TeamId): F[PlayerId] = playerTableService.insert(Player(name, gameId, teamId))

      override def getPlayer(playerId: PlayerId): ErrorOrT[F, Player] = EitherT.fromOptionF(playerTableService.getPlayerById(playerId), NotFoundError("Player"))

      override def changeTeam(playerId: PlayerId, newTeam: TeamId): ErrorOrT[F, Unit] = {
        for {
          player <- getPlayer(playerId)
          _      <- EitherT.liftF(playerTableService.update(player.copy(teamId = newTeam)))
        } yield ()
      }

      override def changeRole(playerId: PlayerId, teamId: TeamId, newRole: PlayerRole): ErrorOrT[F, Unit] = {
        for {
          teamPlayers <- EitherT.liftF(playerTableService.getAllPlayersByTeamId(teamId))
          _           <- EitherT.fromEither[F](_validateRoleChange(teamPlayers, newRole))
          player      <- EitherT.fromOption[F](teamPlayers.find(_.id.equals(playerId)), NotFoundError("Player"))
          _           <- EitherT.liftF(playerTableService.update(player.copy(role = newRole)))
        } yield ()
      }

      private def _validateRoleChange(teamPlayers: Seq[Player], newRole: PlayerRole): Either[GameError, Unit] = {
        newRole match {
          case Spymaster =>
            if (teamPlayers.exists(_.role.equals(Spymaster))) SpymasterAlreadyExistsError.asLeft
            else ().asRight
          case Operative =>
            ().asRight
        }
      }

      override def startGame(playerId: PlayerId): ErrorOrT[F, Unit] = {
        for {
          player <- getPlayer(playerId)
          teams  <- EitherT.liftF(teamProcess.getTeamsIdByGame(player.gameId))
          _      <- _validateOnGameStart(teams)
          _      <- gameProcess.startGame(player.gameId)
        } yield ()
      }

      private def _validateOnGameStart(teamsId: Seq[TeamId]): ErrorOrT[F, Unit] = {
        def validatePlayers(players: Seq[Player]): Either[GameError, Unit] = {
          if (!players.exists(_.role.equals(Spymaster))) TeamWithoutSpymasterError.asLeft
          else if (!players.exists(_.role.equals(Operative))) TeamWithoutOperativesError.asLeft
          else ().asRight
        }

        for {
          teamsPlayers <- EitherT.liftF(teamsId.map(playerTableService.getAllPlayersByTeamId).sequence)
          _            <- EitherT.fromEither[F](teamsPlayers.map(validatePlayers).sequence)
        } yield ()
      }

      override def finishTurn(playerId: PlayerId): ErrorOrT[F, Unit] = {
        for {
          player <- getPlayer(playerId)
          team   <- teamProcess.getTeamById(player.teamId)
          game   <- gameProcess.getGameById(player.gameId)
          _      <- EitherT.fromEither[F](_validateFinishTurnRights(player, team, game))
          _      <- gameProcess.nextTurn(player.gameId)
        } yield ()
      }

      private def _validateFinishTurnRights(player: Player, team: Team, game: Game): Either[GameError, Unit] = {
        if (game.turn.rightForChangingTurn.equals(player.role) && game.turn.teamColor.equals(team.teamColor)) ().asRight
        else CannotChangeTurnError.asLeft
      }

      override def playCard(playerId: PlayerId, cardId: CardId): ErrorOrT[F, Unit] = {
        for {
          player     <- getPlayer(playerId)
          playerTeam <- teamProcess.getTeamById(player.teamId)
          game       <- gameProcess.getGameById(player.gameId)
          card       <- cardProcess.getCardById(cardId)
          _          <- EitherT.liftF(cardProcess.revealCard(card))
          _          <- _processPlayCardResult(game, playerTeam, card)
        } yield ()
      }

      private def _processPlayCardResult(game: Game, team: Team, card: Card): ErrorOrT[F, Unit] = {
        card.cardRole match {
          case CardRole.Agent(teamColor) =>
            for {
              enemyTeam     <- teamProcess.getEnemyTeam(game.id, team.id)
              teamPair      <- EitherT.liftF(
                if (team.teamColor.equals(teamColor)) (team, enemyTeam).pure[F]
                else (enemyTeam, team).pure[F])
              changedTeam   <- EitherT.liftF(teamProcess.changeScore(teamPair._1, Score(1)))
              _             <- EitherT.liftF(
                if (changedTeam.teamScore.equals(game.getMaxScore + (if (changedTeam.teamColor.equals(TeamColor.Red)) Score(1) else Score()))) gameProcess.finishGame(game, changedTeam, teamPair._2)
                else ().pure[F])
            } yield ()
          case CardRole.Innocent => gameProcess.nextTurn(game.id)
          case CardRole.Assassin =>
            for {
              enemyTeam <- teamProcess.getEnemyTeam(game.id, team.id)
              _         <- EitherT.liftF(teamProcess.changeScore(enemyTeam, game.getMaxScore - enemyTeam.teamScore + (if (enemyTeam.teamColor.equals(TeamColor.Red)) Score(1) else Score())))
              _         <- EitherT.liftF(gameProcess.finishGame(game, enemyTeam, team))
            } yield ()
        }
      }
    }
}
