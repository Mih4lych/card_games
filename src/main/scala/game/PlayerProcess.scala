package game

import cats.data.EitherT
import cats.effect.{Async, Ref}
import cats.implicits._
import domain.ID._
import domain._
import domain.GameError._
import domain.PlayerRole._
import service.db.PlayerTableService

trait PlayerProcess[F[_]] {
  def createPlayer(name: String, gameId: GameId, teamId: TeamId): F[PlayerId]
  def getPlayer(playerId: PlayerId): ErrorOrT[F, Player]
  def changeTeam(playerId: PlayerId, newTeam: TeamId): ErrorOrT[F, Unit]
  def changeRole(playerId: PlayerId, teamId: TeamId, newRole: PlayerRole): ErrorOrT[F, Unit]
  def validateOnStart(teamsId: Seq[TeamId]): ErrorOrT[F, Unit]
}
object PlayerProcess {
  def apply[F[_]: Async](playerTableService: PlayerTableService[F]): PlayerProcess[F] =
    new PlayerProcess[F] {
      override def createPlayer(name: String, gameId: GameId, teamId: TeamId): F[PlayerId] = playerTableService.insert(Player(name, gameId, teamId))

      override def getPlayer(playerId: PlayerId): ErrorOrT[F, Player] = EitherT.fromOptionF(playerTableService.getPlayerById(playerId), PlayerNotFoundError)

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
          player      <- EitherT.fromOption[F](teamPlayers.find(_.id.equals(playerId)), PlayerNotFoundError)
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

      override def validateOnStart(teamsId: Seq[TeamId]): ErrorOrT[F, Unit] = {
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
    }
}
