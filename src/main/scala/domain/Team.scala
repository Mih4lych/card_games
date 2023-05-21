package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._

final case class Team(id: TeamId
                     , gameId: GameId
                     , teamColor: TeamColor
                     , teamScore: Score = Score())

object Team {
  implicit val teamEncoder: Encoder[Team] = deriveEncoder[Team]
  implicit val teamDecoder: Decoder[Team] = deriveDecoder[Team]

  def apply(gameId: GameId, color: TeamColor): Team = Team(TeamId(), gameId, color)
}
