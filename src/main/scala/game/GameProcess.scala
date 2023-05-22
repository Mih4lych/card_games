package game

import cats.effect.{Async, Ref}
import domain.Game._
import domain.GameState._
import domain._
import util._

trait GameProcess[F[_]] {
  def startGame(gameRef: Ref[F, Game]): F[Unit]
  def finishGame(gameRef: Ref[F, Game]): F[Unit]
}
object GameProcess {
  def apply[F[_] : Async](cardProcess: CardProcess[F], teamProcess: TeamProcess): GameProcess[F] =
    new GameProcess[F] {
      override def startGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = InProgress))
      override def finishGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = Finished))
    }
}
