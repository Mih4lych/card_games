package domain

import enumeratum.{Enum, EnumEntry}

trait GameState extends EnumEntry

object GameState extends Enum[GameState] {
  val values = findValues

  case object WaitingPayers extends GameState

  case object InProgress extends GameState

  case object Finished /*reason*/ extends GameState
}