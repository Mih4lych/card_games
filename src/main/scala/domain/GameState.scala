package domain

import enumeratum.{Enum, EnumEntry}
import io.circe.{Decoder, Encoder}

sealed trait GameState extends EnumEntry

object GameState extends Enum[GameState] {
  val values: IndexedSeq[GameState] = findValues

  implicit val gameStateEncode: Encoder[GameState] = Encoder[String].contramap {
    case WaitingPayers => "waitingPayers"
    case InProgress => "inProgress"
    case Finished => "finished"
  }

  implicit val gameStateDecode: Decoder[GameState] =
    Decoder
      .decodeString
      .emap {
        case "waitingPayers" => Right(WaitingPayers)
        case "inProgress" => Right(InProgress)
        case "finished" => Right(Finished)
      }

  case object WaitingPayers extends GameState

  case object InProgress extends GameState

  case object Finished extends GameState
}