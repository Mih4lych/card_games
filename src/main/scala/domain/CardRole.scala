package domain

import enumeratum.{Enum, EnumEntry}

sealed abstract class CardRole(color: TeamColor) extends EnumEntry

//trait without color
//Innocent and Assassin
//case class with color parameter for red and blue

object CardRole extends Enum[CardRole] {
  val values: IndexedSeq[CardRole] = findValues

  case object BlueAgent extends CardRole(TeamColor.Blue)

  case object RedAgent extends CardRole(TeamColor.Red)

  case object Innocent extends CardRole(TeamColor.Gray)

  case object Assassin extends CardRole(TeamColor.Black)
}
