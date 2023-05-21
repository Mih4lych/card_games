package domain

import cats.implicits._
import enumeratum.{Enum, EnumEntry}
import io.circe._

import scala.util.Try

sealed trait CardState extends EnumEntry with Product

object CardState extends Enum[CardState] {
  val values: IndexedSeq[CardState] = findValues

  implicit val cardStateEncode: Encoder[CardState] = Encoder[String].contramap(_.entryName)

  implicit val cardStateDecode: Decoder[CardState] =
    Decoder
      .decodeString
      .emap(str => Try(CardState.withName(str)).toEither.leftMap(_.getMessage))

  case object Hidden extends CardState

  case object Revealed extends CardState
}
