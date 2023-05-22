package game

import cats.effect.{Async, Ref}
import domain.Game._
import domain.GameState._
import domain.ID._
import domain._
import service.db.GameTableService

trait GameProcess[F[_]] {
  def createGame(creator: PlayerId): F[GameId]
  def startGame(gameRef: Ref[F, Game]): F[Unit]
  def finishGame(gameRef: Ref[F, Game]): F[Unit]
}
object GameProcess {
  def apply[F[_] : Async](gameTableService: GameTableService[F]): GameProcess[F] =
    new GameProcess[F] {
      override def createGame(creator: PlayerId): F[GameId] = gameTableService.insert(Game(creator))

      override def startGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = InProgress))

      override def finishGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = Finished))
    }
}
