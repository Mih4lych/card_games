package domain

import enumeratum.{Enum, EnumEntry}

sealed trait GameError extends EnumEntry

object GameError extends Enum[GameError] {
  override val values: IndexedSeq[GameError] = findValues

  case object WordServiceConnectionError extends GameError
  case object WordParseError extends GameError
  case object SpymasterAlreadyExistsError extends GameError
  case object CardAlreadyOpenedError extends GameError
  case object PlayerAlreadyInTeamError extends GameError
  case object SpymasterAlreadyInTeamError extends GameError
  case object TeamWithoutOperativesError extends GameError
  case object TeamWithoutSpymasterError extends GameError
}
