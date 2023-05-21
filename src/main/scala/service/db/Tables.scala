package service.db

import domain.CardRole._
import domain.Game._
import domain.ID._
import domain.MoveOrder._
import domain._
import slick.jdbc.PostgresProfile

class Tables(val profile: PostgresProfile) {
  import profile.api._

  implicit val cardIdColumnType = MappedColumnType.base[CardId, String](
    _.id, string => CardId(string))
  implicit val cardStateColumnType = MappedColumnType.base[CardState, String](
    _.entryName, string => CardState.withName(string))
  implicit val cardRoleColumnType = MappedColumnType.base[CardRole, String](
    _.entryName, {
      case "RedAgent" => Agent(TeamColor.Red)
      case "BlueAgent" => Agent(TeamColor.Blue)
      case s if s.equals(Innocent.entryName) => Innocent
      case s if s.equals(Assassin.entryName) => Assassin
    })

  implicit val teamIdColumnType = MappedColumnType.base[TeamId, String](
    _.id, string => TeamId(string))
  implicit val teamColorColumnType = MappedColumnType.base[TeamColor, String](
    _.entryName, string => TeamColor.withName(string))
  implicit val teamScoreColumnType = MappedColumnType.base[Score, Int](
    _.score, int => Score(int))

  implicit val playerIdColumnType = MappedColumnType.base[PlayerId, String](
    _.id, string => PlayerId(string))
  implicit val playerRoleColumnType = MappedColumnType.base[PlayerRole, String](
    _.entryName, string => PlayerRole.withName(string))

  implicit val gameIdColumnType = MappedColumnType.base[GameId, String](
    _.id, string => GameId(string))
  implicit val gameStateColumnType = MappedColumnType.base[GameState, String](
    _.entryName, string => GameState.withName(string))
  implicit val wordsCountColumnType = MappedColumnType.base[GameWordsCount, Int](
    _.count, int => GameWordsCount(int))
  implicit val moveOrderColumnType = MappedColumnType.base[MoveOrder, String](
    _.entryName, {
      case "RedSpymasterMove" => SpymasterMove(TeamColor.Red)
      case "BlueSpymasterMove" => SpymasterMove(TeamColor.Blue)
      case "RedOperativesMove" => OperativesMove(TeamColor.Red)
      case "BlueOperativesMove" => OperativesMove(TeamColor.Blue)
    })

  implicit val resultIdColumnType = MappedColumnType.base[ResultId, String](
    _.id, string => ResultId(string))

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