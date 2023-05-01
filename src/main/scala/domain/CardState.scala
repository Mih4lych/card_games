package domain

import enumeratum.{Enum, EnumEntry}

sealed trait CardState extends EnumEntry

object CardState extends Enum[CardState] {
  val values = findValues

  case object Closed extends CardState

  case object Opened extends CardState
}
