package domain

import enumeratum.{Enum, EnumEntry}

sealed trait MoveOrder extends EnumEntry

object MoveOrder extends Enum[MoveOrder] {
  val values: IndexedSeq[MoveOrder] = findValues

  object RedSpymasterMove extends MoveOrder
  object BlueSpymasterMove extends MoveOrder
  object RedTeamMove extends MoveOrder
  object BlueTeamMove extends MoveOrder
}
