package game

import cats.effect.{Async, Ref}
import domain.GameState._
import domain.MoveOrder._
import domain.TeamColor._
import domain._
import util._

trait GameProcess[F[_]] {
  def startGame(gameRef: Ref[F, Game]): F[Unit]
  def finishGame(gameRef: Ref[F, Game]): F[Unit]
  def changeMoveOrder(gameRef: Ref[F, Game]): F[Unit]
}
object GameProcess {
  def apply[F[_] : Async](cardProcess: CardProcess[F], teamProcess: TeamProcess): GameProcess[F] =
    new GameProcess[F] {
      override def startGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = InProgress))

      override def finishGame(gameRef: Ref[F, Game]): F[Unit] = gameRef.update(game => game.copy(gameState = Finished))

      override def changeMoveOrder(gameRef: Ref[F, Game]): F[Unit] =
        gameRef
          .update(game => game.copy(moveOrder =
            game.moveOrder match {
              case SpymasterMove(teamColor) => OperativesMove(teamColor)
              case OperativesMove(teamColor) if teamColor.equals(Red) => SpymasterMove(Blue)
              case OperativesMove(teamColor) if teamColor.equals(Blue) => SpymasterMove(Red)
            }))
    }
}
