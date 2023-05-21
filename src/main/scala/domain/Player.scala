package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._

final case class Player(id: PlayerId, name: String, gameId: GameId, teamId: TeamId, role: PlayerRole = PlayerRole.Operative) {
  def changeRole(playerRole: PlayerRole): Player = copy(role = playerRole)
}

object Player {
  implicit val playerEncoder: Encoder[Player] = deriveEncoder[Player]
  implicit val playerDecoder: Decoder[Player] = deriveDecoder[Player]

  def apply(name: String, gameId: GameId, teamId: TeamId): Player = {
    Player(PlayerId(), name, gameId, teamId)
  }
}