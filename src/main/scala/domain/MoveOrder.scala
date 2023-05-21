package domain

import enumeratum.{Enum, EnumEntry}
import io.circe.{Decoder, Encoder}

sealed trait MoveOrder extends EnumEntry

object MoveOrder extends Enum[MoveOrder] {
  val values: IndexedSeq[MoveOrder] = findValues

  implicit val moveOrderEncode: Encoder[MoveOrder] = Encoder[String].contramap(_.entryName)

  implicit val moveOrderDecode: Decoder[MoveOrder] =
    Decoder
      .decodeString
      .emap {
        case "RedSpymasterMove" => Right(SpymasterMove(TeamColor.Red))
        case "BlueSpymasterMove" => Right(SpymasterMove(TeamColor.Blue))
        case "RedOperativesMove" => Right(OperativesMove(TeamColor.Red))
        case "BlueOperativesMove" => Right(OperativesMove(TeamColor.Blue))
      }

  case class SpymasterMove(teamColor: TeamColor) extends MoveOrder {
    override def entryName: String = s"${teamColor.entryName}SpymasterMove"
  }
  case class OperativesMove(teamColor: TeamColor) extends MoveOrder {
    override def entryName: String = s"${teamColor.entryName}OperativesMove"
  }
}
