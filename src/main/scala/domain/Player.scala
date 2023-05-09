package domain

import cats.effect.kernel.Sync
import cats.implicits._

final case class Player(id: ID, name: String, role: PlayerRole = PlayerRole.Spectator)

object Player {
  def create[F[_] : Sync](name: String): F[Player] = {
    ID().map(playerId => Player(playerId, name))
  }
}