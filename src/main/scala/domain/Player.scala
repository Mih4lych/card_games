package domain

import cats.effect.kernel.Sync
import cats.implicits._
import domain.ID.PlayerId

final case class Player(id: PlayerId, name: String, role: PlayerRole = PlayerRole.Spectator)

object Player {
  def create[F[_] : Sync](name: String): F[Player] = {
    PlayerId().map(playerId => Player(playerId, name))
  }
}