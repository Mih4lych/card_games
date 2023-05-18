package domain

import cats.effect.Sync
import cats.syntax.all._
import domain.Game._
import domain.ID._
import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._


//round (game, card(opened or all), move)
final case class Game(id: GameId
                     , gameCreator: Player
                     , gameState: GameState = GameState.WaitingPayers
                     , cards: Seq[Card] = Vector.empty[Card]
                     , wordsCount: GameWordsCount = GameWordsCount()
                     , blueTeam: Team = Team(TeamColor.Blue)
                     , redTeam: Team = Team(TeamColor.Red)
                     , blueScore: TeamScore = TeamScore()
                     , redScore: TeamScore = TeamScore()
                     , moveOrder: MoveOrder = MoveOrder.Empty) {

  /*def connectPlayer(player: Player): Game = this.copy(players = player +: this.players)
  def disconnectPlayer(player: Player): Game = this.copy(players = this.players.filterNot(_.equals(player)))
  def disconnectPlayerByID(playerID: ID): Game = this.copy(players = this.players.filterNot(_.id.equals(playerID)))
  def setWordCount(count: Int): Game = this.copy(wordsCount = GameWordsCount(count))*/
}

object Game {
  final case class TeamScore(score: Int = 0) extends AnyVal
  object TeamScore {
    implicit val teamScoreEncoder: Encoder[TeamScore] = Encoder.instance(_.score.asJson)
    implicit val teamScoreDecoder: Decoder[TeamScore] = Decoder[Int].map(TeamScore(_))
  }

  final case class GameWordsCount(count: Int = 0) extends AnyVal
  object GameWordsCount {
    implicit val gameWordsCountEncoder: Encoder[GameWordsCount] = Encoder.instance(_.count.asJson)
    implicit val gameWordsCountDecoder: Decoder[GameWordsCount] = Decoder[Int].map(GameWordsCount(_))
  }

  implicit val gameEncoder: Encoder[Game] = deriveEncoder[Game]
  implicit val gameDecoder: Decoder[Game] = deriveDecoder[Game]

  def apply(creator: Player): Game = {
    Game(id = GameId(), gameCreator = creator)
  }
}