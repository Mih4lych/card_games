package service.db

import ColumnMappers._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class PlayerTable(tag: Tag) extends Table[Player](tag, "Player") {
  def id = column[PlayerId]("id", O.PrimaryKey)

  def name = column[String]("name")

  def gameId = column[GameId]("gameId")

  def teamId = column[TeamId]("teamId")

  def role = column[PlayerRole]("role")

  override def * = (id, name, gameId, teamId, role) <> (Function.tupled(Player.apply), Player.unapply)
}

object PlayerTable extends TableQuery(new PlayerTable(_)) {
  lazy val byId = Compiled((id: Rep[PlayerId]) => filter(_.id === id))
  lazy val byGameId = Compiled((id: Rep[GameId]) => filter(_.gameId === id))
  lazy val byTeamId = Compiled((id: Rep[TeamId]) => filter(_.teamId === id))
}
