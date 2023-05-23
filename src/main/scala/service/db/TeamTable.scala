package service.db

import ColumnMappers._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class TeamTable(tag: Tag) extends Table[Team](tag, "Team") {
  def id = column[TeamId]("id", O.PrimaryKey)

  def gameId = column[GameId]("gameId")

  def teamColor = column[TeamColor]("teamColor")

  def teamScore = column[Score]("teamScore")

  override def * = (id, gameId, teamColor, teamScore) <> (Function.tupled(Team.apply), Team.unapply)
}

object TeamTable extends TableQuery(new TeamTable(_)) {
  lazy val byId = Compiled((id: Rep[TeamId]) => filter(_.id === id))
  lazy val byGameId = Compiled((id: Rep[GameId]) => filter(_.gameId === id))
}