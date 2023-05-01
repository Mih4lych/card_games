package domain

import cats.effect.kernel.Sync
import cats.implicits._

import java.util.UUID
import scala.reflect.runtime.universe._

sealed trait ID

final case class CardId(id: String) extends AnyVal with ID
final case class GameId(id: String) extends AnyVal with ID
final case class PlayerId(id: String) extends AnyVal with ID

object ID {
  def apply[F[-_]: Sync, A <: ID : TypeTag]: F[A] = {
    Sync[F]
      .delay(UUID.randomUUID().toString)
      .map { id =>
        typeOf[A] match {
          case _: CardId => CardId(id)
          case _: GameId => GameId(id)
          case _: PlayerId => PlayerId(id)
        }
      }
  }
}
