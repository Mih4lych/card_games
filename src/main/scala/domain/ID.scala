package domain

import enumeratum.{Enum, EnumEntry}
import io.circe._
import io.circe.syntax._

import java.util.UUID

sealed trait ID extends EnumEntry {
  val id: String
}

object ID extends Enum[ID] {
  override def values: IndexedSeq[ID] = findValues

  case class CardId(id: String) extends ID
  object CardId {
    implicit val cardIdEncoder: Encoder[CardId] = Encoder.instance(_.id.asJson)
    implicit val cardIdDecoder: Decoder[CardId] = Decoder[String].map(CardId(_))

    def apply(): CardId = nextId(CardId(_))
  }

  case class GameId(id: String) extends ID
  object GameId {
    implicit val gameIdEncoder: Encoder[GameId] = Encoder.instance(_.id.asJson)
    implicit val gameIdDecoder: Decoder[GameId] = Decoder[String].map(GameId(_))

    def apply(): GameId = nextId(GameId(_))
  }

  case class PlayerId(id: String) extends ID
  object PlayerId {
    implicit val playerIdEncoder: Encoder[PlayerId] = Encoder.instance(_.id.asJson)
    implicit val playerIdDecoder: Decoder[PlayerId] = Decoder[String].map(PlayerId(_))

    def apply(): PlayerId = nextId(PlayerId(_))
  }

  case class TeamId(id: String) extends ID
  object TeamId {
    implicit val teamIdIdEncoder: Encoder[TeamId] = Encoder.instance(_.id.asJson)
    implicit val teamIdIdDecoder: Decoder[TeamId] = Decoder[String].map(TeamId(_))

    def apply(): TeamId = nextId(TeamId(_))
  }

  case class ResultId(id: String) extends ID
  object ResultId {
    implicit val teamIdIdEncoder: Encoder[ResultId] = Encoder.instance(_.id.asJson)
    implicit val teamIdIdDecoder: Decoder[ResultId] = Decoder[String].map(ResultId(_))

    def apply(): ResultId = nextId(ResultId(_))
  }

  private def nextId[A <: ID](f: String => A): A =
    f(UUID.randomUUID().toString)
}