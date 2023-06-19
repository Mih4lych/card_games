package service.db
import domain._
import domain.CardRole._
import domain.Game._
import domain.ID._
import domain.Turn._
import slick.jdbc.PostgresProfile.api._

object ColumnMappers {
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
  implicit val moveOrderColumnType = MappedColumnType.base[Turn, String](
    _.entryName, {
      case "RedSpymasterMove" => SpymasterTurn(TeamColor.Red)
      case "BlueSpymasterMove" => SpymasterTurn(TeamColor.Blue)
      case "RedOperativesMove" => OperativesTurn(TeamColor.Red)
      case "BlueOperativesMove" => OperativesTurn(TeamColor.Blue)
    })

  implicit val resultIdColumnType = MappedColumnType.base[ResultId, String](
    _.id, string => ResultId(string))
}
