package domain

import cats.effect.Sync
import cats.syntax.all._
import domain.Game._
import domain.ID._
import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

final case class Game(id: GameId
                      , gameState: GameState = GameState.WaitingPayers
                      , wordsCount: GameWordsCount = GameWordsCount()
                      , turn: Turn = Turn.OperativesTurn(TeamColor.Red)) {
  def getMaxScore: Score = Score((wordsCount.count - 1) / 3)
}

object Game {

  final case class GameWordsCount(count: Int = 0) extends AnyVal
  object GameWordsCount {
    implicit val gameWordsCountEncoder: Encoder[GameWordsCount] = Encoder.instance(_.count.asJson)
    implicit val gameWordsCountDecoder: Decoder[GameWordsCount] = Decoder[Int].map(GameWordsCount(_))
  }

  implicit val gameEncoder: Encoder[Game] = deriveEncoder[Game]
  implicit val gameDecoder: Decoder[Game] = deriveDecoder[Game]

  def apply(): Game = {
    Game(id = GameId())
  }
}