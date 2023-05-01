package domain

import enumeratum.{Enum, EnumEntry}

sealed trait CardState extends EnumEntry

object CardState extends Enum[CardState] {
  val values: IndexedSeq[CardState] = findValues

  case object Closed extends CardState

  case object Opened extends CardState
}
