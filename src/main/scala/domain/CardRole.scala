package domain

import enumeratum.{Enum, EnumEntry}
import io.circe._
import io.circe.syntax._

sealed trait CardRole extends EnumEntry

object CardRole extends Enum[CardRole] {
  val values: IndexedSeq[CardRole] = findValues

  implicit val cardRoleEncode: Encoder[CardRole] = Encoder.instance {
    case _@ Agent(TeamColor.Red) => "redAgent".asJson
    case _@ Agent(TeamColor.Blue) => "blueAgent".asJson
    case Innocent => "innocent".asJson
    case Assassin => "assassin".asJson
  }

  implicit val cardRoleDecode: Decoder[CardRole] =
    Decoder
      .decodeString
      .emap {
        case "redAgent" => Right(Agent(TeamColor.Red))
        case "blueAgent" => Right(Agent(TeamColor.Blue))
        case "innocent" => Right(Innocent)
        case "assassin" => Right(Assassin)
        case _ => Left("Wrong card state")
      }

  case class Agent(teamColor: TeamColor) extends CardRole
  case object Innocent extends CardRole
  case object Assassin extends CardRole
}
