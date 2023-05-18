package domain

import enumeratum._
import io.circe._

sealed trait TeamColor extends EnumEntry

object TeamColor extends Enum[TeamColor] {
  val values: IndexedSeq[TeamColor] = findValues

  implicit val teamColorEncode: Encoder[TeamColor] = Encoder[String].contramap {
    case Blue => "blue"
    case Red => "red"
  }

  implicit val teamColorDecode: Decoder[TeamColor] =
    Decoder
      .decodeString
      .emap {
        case "blue" => Right(Blue)
        case "red" => Right(Red)
      }

  case object Blue extends TeamColor

  case object Red extends TeamColor
}
