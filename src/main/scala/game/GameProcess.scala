package game

import cats.effect.std._
import cats.effect.{Async, Ref}
import domain._

trait GameProcess[F[_]] {
  def createGame(creator: Player): F[Ref[F, Game]]
}
object GameProcess {
  def apply[F[_]: Async: Random]: GameProcess[F] =
    new GameProcess[F] {
      override def createGame(creator: Player): F[Ref[F, Game]] = Ref.of(Game(creator))
    }
}
