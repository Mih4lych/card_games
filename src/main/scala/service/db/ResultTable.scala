package service.db

import ColumnMappers._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class ResultTable(tag: Tag) extends Table[Result](tag, "Result") {
  def id = column[ResultId]("id", O.PrimaryKey)

  def gameId = column[GameId]("gameId")

  def winningTeam = column[TeamId]("winningTeam")

  def blueTeamScore = column[Score]("blueTeamScore")

  def redTeamScore = column[Score]("redTeamScore")

  override def * = (id, gameId, winningTeam, blueTeamScore, redTeamScore) <> (Function.tupled(Result.apply), Result.unapply)
}

object ResultTable extends TableQuery(new ResultTable(_)) {
  lazy val table = TableQuery[ResultTable]
  lazy val byId = Compiled((id: Rep[ResultId]) => filter(_.id === id))
  lazy val byGameId = Compiled((id: Rep[GameId]) => filter(_.gameId === id))
  lazy val byTeamId = Compiled((id: Rep[TeamId]) => filter(_.winningTeam === id))
}
