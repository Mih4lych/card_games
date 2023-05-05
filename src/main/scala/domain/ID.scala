package domain

import cats.effect.kernel.Sync
import cats.implicits._
import enumeratum.{Enum, EnumEntry}

import java.util.UUID

sealed trait ID extends EnumEntry

object ID extends Enum[ID] {
  override def values: IndexedSeq[ID] = findValues

  case class CardId(id: String) extends ID
  object CardId {
    def apply[F[_]: Sync](): F[CardId] = nextId.map(CardId(_))
  }

  case class GameId(id: String) extends ID
  object GameId {
    def apply[F[_]: Sync](): F[GameId] = nextId.map(GameId(_))
  }

  case class PlayerId(id: String) extends ID
  object PlayerId {
    def apply[F[_]: Sync](): F[PlayerId] = nextId.map(PlayerId(_))
  }

  def nextId[F[_]: Sync]: F[String] =
    Sync[F].delay(UUID.randomUUID().toString)
}
