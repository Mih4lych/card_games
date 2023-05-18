package domain

import enumeratum.{Enum, EnumEntry}
import io.circe._
sealed trait CardState extends EnumEntry

object CardState extends Enum[CardState] {
  val values: IndexedSeq[CardState] = findValues

  implicit val cardStateEncode: Encoder[CardState] = Encoder[String].contramap {
    case Closed => "closed"
    case Opened => "opened"
  }

  implicit val cardStateDecode: Decoder[CardState] =
    Decoder
      .decodeString
      .emap {
        case "closed" => Right(Closed)
        case "opened" => Right(Opened)
      }

  case object Closed extends CardState

  case object Opened extends CardState
}
