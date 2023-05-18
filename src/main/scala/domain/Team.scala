package domain

import io.circe._

final case class Team(teamColor: TeamColor
                     , players: Seq[Player]
                     , hasSpymaster: Boolean = false)

object Team {
  implicit val teamEncoder: Encoder[Team] =
    Encoder.forProduct3("teamColor", "players", "hasSpymaster")(s => (s.teamColor, s.players, s.hasSpymaster))
  implicit val teamDecoder: Decoder[Team] =
    Decoder.forProduct3("teamColor", "players", "hasSpymaster")(Team.apply)

  def apply(color: TeamColor): Team = Team(color, Vector.empty[Player])
}
