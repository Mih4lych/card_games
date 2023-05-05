package domain

import enumeratum.{Enum, EnumEntry}

sealed trait MoveOrder extends EnumEntry

object MoveOrder extends Enum[MoveOrder] {
  val values: IndexedSeq[MoveOrder] = findValues

  case object RedSpymasterMove extends MoveOrder
  case object BlueSpymasterMove extends MoveOrder
  case object RedTeamMove extends MoveOrder
  case object BlueTeamMove extends MoveOrder
}
