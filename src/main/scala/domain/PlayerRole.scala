package domain

import enumeratum.{Enum, EnumEntry}
import io.circe._

sealed trait PlayerRole extends EnumEntry

object PlayerRole extends Enum[PlayerRole] {
  val values: IndexedSeq[PlayerRole] = findValues

  implicit val playerRoleEncode: Encoder[PlayerRole] = Encoder[String].contramap {
    case Spymaster => "spymaster"
    case Operative => "operative"
  }

  implicit val playerRoleDecode: Decoder[PlayerRole] =
    Decoder
      .decodeString
      .emap {
        case "spymaster" => Right(Spymaster)
        case "operative" => Right(Operative)
      }

  case object Spymaster extends PlayerRole

  case object Operative extends PlayerRole
}