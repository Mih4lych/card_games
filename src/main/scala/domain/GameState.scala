package domain

import enumeratum.{Enum, EnumEntry}

sealed trait GameState extends EnumEntry

object GameState extends Enum[GameState] {
  val values: IndexedSeq[GameState] = findValues

  case object WaitingPayers extends GameState

  case object InProgress extends GameState

  case object Finished /*reason*/ extends GameState
}