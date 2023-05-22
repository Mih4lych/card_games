package service.db

import domain.CardRole._
import domain.Game._
import domain.ID._
import domain.MoveOrder._
import domain._
import slick.jdbc.PostgresProfile
import ColumnMappers._

class Tables(val profile: PostgresProfile) {
  import profile.api._

  class CardTable(tag: Tag) extends Table[Card](tag, Some("game"), "Card") {
    def id = column[CardId]("id", O.PrimaryKey)
    def gameId = column[GameId]("gameId")
    def word = column[String]("word")
    def cardRole = column[CardRole]("cardRole")
    def cardState = column[CardState]("cardState")

    def gameFK = foreignKey("card_game_fk", gameId, TableQuery[GameTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (id, gameId, word, cardRole, cardState) <> (Function.tupled(Card.apply), Card.unapply)
  }

  class TeamTable(tag: Tag) extends Table[Team](tag, Some("game"), "Team") {
    def id = column[TeamId]("id", O.PrimaryKey)
    def gameId = column[GameId]("gameId")
    def teamColor = column[TeamColor]("teamColor")
    def teamScore = column[Score]("teamScore")

    def gameFK = foreignKey("team_game_fk", gameId, TableQuery[GameTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (id, gameId, teamColor, teamScore) <> (Function.tupled(Team.apply), Team.unapply)
  }

  class PlayerTable(tag: Tag) extends Table[Player](tag, Some("game"), "Player") {
    def id = column[PlayerId]("id", O.PrimaryKey)
    def name = column[String]("name")
    def gameId = column[GameId]("gameId")
    def teamId = column[TeamId]("teamId")
    def role = column[PlayerRole]("role")

    def gameFK = foreignKey("player_game_fk", gameId, TableQuery[GameTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def teamFK = foreignKey("player_team_fk", teamId, TableQuery[TeamTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (id, name, gameId, teamId, role) <> (Function.tupled(Player.apply), Player.unapply)
  }

  class GameTable(tag: Tag) extends Table[Game](tag, Some("game"), "Game") {
    def id = column[GameId]("id", O.PrimaryKey)
    def gameCreator = column[PlayerId]("gameCreator")
    def gameState = column[GameState]("gameState")
    def wordsCount = column[GameWordsCount]("wordsCount")
    def moveOrder = column[MoveOrder]("moveOrder")

    override def * = (id, gameCreator, gameState, wordsCount, moveOrder) <> (Function.tupled(Game.apply), Game.unapply)
  }

  class ResultTable(tag: Tag) extends Table[Result](tag, Some("game"), "Result") {
    def id = column[ResultId]("id", O.PrimaryKey)
    def gameId = column[GameId]("gameCreator")
    def winningTeam = column[TeamId]("winningTeam")
    def blueTeamScore = column[Score]("blueTeamScore")
    def redTeamScore = column[Score]("redTeamScore")

    def gameFK = foreignKey("result_game_fk", gameId, TableQuery[GameTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def winningTeamFK = foreignKey("result_winningTeam_fk", winningTeam, TableQuery[TeamTable])(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

    override def * = (id, gameId, winningTeam, blueTeamScore, redTeamScore) <> (Function.tupled(Result.apply), Result.unapply)
  }
}