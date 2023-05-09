package game

import domain.{GameError, Player, PlayerRole}

trait PlayerProcess {
  def changeRole(player: Player, playerRole: PlayerRole): Player
}
object PlayerProcess {
  def apply: PlayerProcess =
    new PlayerProcess {
      override def changeRole(player: Player, playerRole: PlayerRole): Player = player.copy(role = playerRole)
    }
}
