package game

import cats.syntax.semigroup._
import domain._

trait TeamProcess {
  def changeScore(team: Team, points: Score): Team
}

object TeamProcess {
  def apply(): TeamProcess =
    new TeamProcess {
      override def changeScore(team: Team, points: Score): Team = team.copy(teamScore = team.teamScore |+| points)
    }
}
