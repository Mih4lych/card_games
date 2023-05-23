package service.db

import ColumnMappers._
import domain.Game.GameWordsCount
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class GameTable(tag: Tag) extends Table[Game](tag, Some("game"), "Game") {
  def id = column[GameId]("id", O.PrimaryKey)
  def gameCreator = column[PlayerId]("gameCreator")
  def gameState = column[GameState]("gameState")
  def wordsCount = column[GameWordsCount]("wordsCount")
  def moveOrder = column[MoveOrder]("moveOrder")

  override def * = (id, gameCreator, gameState, wordsCount, moveOrder) <> (Function.tupled(Game.apply), Game.unapply)
}

object GameTable extends TableQuery(new GameTable(_)) {
  lazy val byId = Compiled((id: Rep[GameId]) => filter(_.id === id))
  lazy val byCreatorId = Compiled((id: Rep[PlayerId]) => filter(_.gameCreator === id))
}
