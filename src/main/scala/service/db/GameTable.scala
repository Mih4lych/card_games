package service.db

import ColumnMappers._
import domain.Game.GameWordsCount
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class GameTable(tag: Tag) extends Table[Game](tag, "Game") {
  def id = column[GameId]("id", O.PrimaryKey)
  def gameState = column[GameState]("gameState")
  def wordsCount = column[GameWordsCount]("wordsCount")
  def turn = column[Turn]("turn")

  override def * = (id, gameState, wordsCount, turn) <> (Function.tupled(Game.apply), Game.unapply)
}

object GameTable extends TableQuery(new GameTable(_)) {
  lazy val byId = Compiled((id: Rep[GameId]) => filter(_.id === id))
}
