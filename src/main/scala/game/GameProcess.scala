package game

import cats.data.EitherT
import cats.syntax.all._
import cats.effect.Async
import domain.GameError._
import domain.GameState._
import domain.ID._
import domain._
import service.GameCache

trait GameProcess[F[_]] {
  def createGame(): F[GameId]
  def startGame(gameId: GameId): ErrorOrT[F, Unit]
  def finishGame(game: Game, winningTeam: Team, losingTeam: Team): F[Unit]
  def nextTurn(gameId: GameId): ErrorOrT[F, Unit]
  def getGameById(gameId: GameId): ErrorOrT[F, Game]
}
object GameProcess {
  def apply[F[_] : Async](gameCache: GameCache[F], cardProcess: CardProcess[F], resultProcess: ResultProcess[F]): GameProcess[F] =
    new GameProcess[F] {
      override def createGame(): F[GameId] = gameCache.insert(Game())

      override def startGame(gameId: GameId): ErrorOrT[F, Unit] = {
        for {
          game    <- getGameById(gameId)
          _       <- EitherT.liftF(cardProcess.makeCards(game))
          _       <- EitherT.liftF(gameCache.update(game.copy(gameState = InProgress)))
        } yield ()
      }

      override def finishGame(game: Game, winningTeam: Team, losingTeam: Team): F[Unit] = {
        val (redScore, blueScore) = if (winningTeam.teamColor.equals(TeamColor.Red)) (winningTeam.teamScore, losingTeam.teamScore) else (losingTeam.teamScore, winningTeam.teamScore)

        for {
          _ <- gameCache.update(game.copy(gameState = Finished))
          _ <- resultProcess.createResult(game.id, winningTeam.id, redScore, blueScore)
        } yield ()
      }

      override def nextTurn(gameId: GameId): ErrorOrT[F, Unit] = {
        for {
          game <- getGameById(gameId)
          _    <- EitherT.liftF(gameCache.update(game.copy(turn = Turn.nextTurn(game.turn))))
        } yield ()
      }

      override def getGameById(gameId: GameId): ErrorOrT[F, Game] = EitherT.fromOptionF(gameCache.get(gameId), NotFoundError("Game"))
    }
}
