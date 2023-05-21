package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._

case class Result(id: ResultId
                 , gameId: GameId
                 , winningTeam: TeamId
                 , blueTeamScore: Score
                 , redTeamScore: Score)

object Result {
  implicit val gameEncoder: Encoder[Game] = deriveEncoder[Game]
  implicit val gameDecoder: Decoder[Game] = deriveDecoder[Game]

  def apply(gameId: GameId, winningTeam: TeamId, blueTeamScore: Score, redTeamScore: Score): Result =
    Result(ResultId(), gameId, winningTeam, blueTeamScore, redTeamScore)
}
