package domain

import io.circe._
import io.circe.generic.semiauto._

final case class Team(teamColor: TeamColor
                     , players: Seq[Player]
                     , hasSpymaster: Boolean = false)

object Team {
  implicit val teamEncoder: Encoder[Team] = deriveEncoder[Team]
  implicit val teamDecoder: Decoder[Team] = deriveDecoder[Team]

  def apply(color: TeamColor): Team = Team(color, Vector.empty[Player])
}
