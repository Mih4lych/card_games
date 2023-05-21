package domain

import cats.implicits._
import enumeratum._
import io.circe._

import scala.util.Try


sealed trait TeamColor extends EnumEntry

object TeamColor extends Enum[TeamColor] {
  val values: IndexedSeq[TeamColor] = findValues

  implicit val teamColorEncode: Encoder[TeamColor] = Encoder[String].contramap(_.entryName)

  implicit val teamColorDecode: Decoder[TeamColor] =
    Decoder
      .decodeString
      .emap(str => Try(TeamColor.withName(str)).toEither.leftMap(_.getMessage))

  case object Blue extends TeamColor

  case object Red extends TeamColor
}
