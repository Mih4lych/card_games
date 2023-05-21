package domain

import enumeratum.{Enum, EnumEntry}
import io.circe._
import io.circe.syntax._

sealed trait CardRole extends EnumEntry

object CardRole extends Enum[CardRole] {
  val values: IndexedSeq[CardRole] = findValues

  implicit val cardRoleEncode: Encoder[CardRole] = Encoder[String].contramap(_.entryName)

  implicit val cardRoleDecode: Decoder[CardRole] =
    Decoder
      .decodeString
      .emap {
        case "RedAgent" => Right(Agent(TeamColor.Red))
        case "BlueAgent" => Right(Agent(TeamColor.Blue))
        case f if f.equals(Innocent.entryName) => Right(Innocent)
        case f if f.equals(Assassin.entryName) => Right(Assassin)
      }

  case class Agent(teamColor: TeamColor) extends CardRole {
    override def entryName: String = s"${teamColor.entryName}Agent"
  }
  case object Innocent extends CardRole
  case object Assassin extends CardRole
}
