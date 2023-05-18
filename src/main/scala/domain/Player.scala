package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._
final case class Player(id: PlayerId, name: String, gameId: GameId, role: PlayerRole = PlayerRole.Operative) {
  def changeRole(playerRole: PlayerRole): Player = copy(role = playerRole)
}

object Player {
  implicit val playerEncoder: Encoder[Player] =
    Encoder.forProduct4("id", "name", "gameId", "role")(s => (s.id, s.name, s.gameId, s.role))
  implicit val playerDecoder: Decoder[Player] =
    Decoder.forProduct4("id", "name", "gameId", "role")(Player.apply)

  def apply(name: String, gameId: GameId): Player = {
    Player(PlayerId(), name, gameId)
  }
}