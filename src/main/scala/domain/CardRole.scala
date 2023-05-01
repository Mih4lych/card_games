package domain

import enumeratum.{Enum, EnumEntry}

sealed case class CardRole(color: TeamColor) extends EnumEntry

object CardRole extends Enum[CardRole] {
  val values: IndexedSeq[CardRole] = findValues

  case object BlueAgent extends CardRole(TeamColor.Blue)

  case object RedAgent extends CardRole(TeamColor.Red)

  case object Innocent extends CardRole(TeamColor.Gray)

  case object Assassin extends CardRole(TeamColor.Black)
}
