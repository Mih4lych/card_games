package domain

import enumeratum.{Enum, EnumEntry}
import io.circe.{Decoder, Encoder}

sealed trait MoveOrder extends EnumEntry

object MoveOrder extends Enum[MoveOrder] {
  val values: IndexedSeq[MoveOrder] = findValues

  implicit val moveOrderEncode: Encoder[MoveOrder] = Encoder[String].contramap {
    case SpymasterMove(TeamColor.Red) => "redSpymasterMove"
    case SpymasterMove(TeamColor.Blue) => "blueSpymasterMove"
    case OperativesMove(TeamColor.Red) => "redOperativesMove"
    case OperativesMove(TeamColor.Blue) => "blueOperativesMove"
    case Empty => "empty"
  }

  implicit val moveOrderDecode: Decoder[MoveOrder] =
    Decoder
      .decodeString
      .emap {
        case "redSpymasterMove" => Right(SpymasterMove(TeamColor.Red))
        case "blueSpymasterMove" => Right(SpymasterMove(TeamColor.Blue))
        case "redOperativesMove" => Right(OperativesMove(TeamColor.Red))
        case "blueOperativesMove" => Right(OperativesMove(TeamColor.Blue))
        case "empty" => Right(Empty)
      }

  case class SpymasterMove(teamColor: TeamColor) extends MoveOrder
  case class OperativesMove(teamColor: TeamColor) extends MoveOrder
  case object Empty extends MoveOrder
}
