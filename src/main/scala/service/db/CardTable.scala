package service.db

import ColumnMappers._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._

class CardTable(tag: Tag) extends Table[Card](tag, "Card") {
  def id = column[CardId]("id", O.PrimaryKey)

  def gameId = column[GameId]("gameId")

  def word = column[String]("word")

  def cardRole = column[CardRole]("cardRole")

  def cardState = column[CardState]("cardState")

  override def * = (id, gameId, word, cardRole, cardState) <> (Function.tupled(Card.apply), Card.unapply)
}

object CardTable extends TableQuery(new CardTable(_)) {
  lazy val table = TableQuery[CardTable]
  lazy val byId = Compiled((id: Rep[CardId]) => filter(_.id === id))
  lazy val byGameId = Compiled((id: Rep[GameId]) => filter(_.gameId === id))
}


