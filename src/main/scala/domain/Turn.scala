package domain

import enumeratum.{Enum, EnumEntry}
import io.circe.{Decoder, Encoder}

sealed trait Turn extends EnumEntry {
  val teamColor: TeamColor

  def rightForChangingTurn: PlayerRole
}

object Turn extends Enum[Turn] {
  val values: IndexedSeq[Turn] = findValues

  implicit val moveOrderEncode: Encoder[Turn] = Encoder[String].contramap(_.entryName)

  implicit val moveOrderDecode: Decoder[Turn] =
    Decoder
      .decodeString
      .emap {
        case "RedSpymasterTurn"   => Right(SpymasterTurn(TeamColor.Red))
        case "BlueSpymasterTurn"  => Right(SpymasterTurn(TeamColor.Blue))
        case "RedOperativesTurn"  => Right(OperativesTurn(TeamColor.Red))
        case "BlueOperativesTurn" => Right(OperativesTurn(TeamColor.Blue))
      }

  case class SpymasterTurn(teamColor: TeamColor) extends Turn {
    override def entryName: String = s"${teamColor.entryName}SpymasterTurn"

    override val rightForChangingTurn: PlayerRole = PlayerRole.Spymaster
  }
  case class OperativesTurn(teamColor: TeamColor) extends Turn {
    override def entryName: String = s"${teamColor.entryName}OperativesTurn"

    override val rightForChangingTurn: PlayerRole = PlayerRole.Operative
  }

  def nextTurn(curTurn: Turn): Turn = {
    curTurn match {
      case SpymasterTurn(teamColor) => OperativesTurn(teamColor)
      case OperativesTurn(teamColor) =>
        teamColor match {
          case TeamColor.Blue => SpymasterTurn(TeamColor.Red)
          case TeamColor.Red  => SpymasterTurn(TeamColor.Blue)
        }
    }
  }
}
