package domain

import enumeratum.{Enum, EnumEntry}

sealed abstract class PlayerRole(val color: TeamColor) extends EnumEntry

object PlayerRole extends Enum[GameState] {
  val values = findValues

  case object SpymasterRed extends PlayerRole(TeamColor.Red)

  case object SpymasterBlue extends PlayerRole(TeamColor.Blue)

  case object TeamRed extends PlayerRole(TeamColor.Red)

  case object TeamBlue extends PlayerRole(TeamColor.Blue)

  case object Spectator extends PlayerRole(TeamColor.Empty)
}