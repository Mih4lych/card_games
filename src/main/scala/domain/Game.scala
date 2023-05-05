package domain

import domain.Game._
import domain.ID.GameId

final case class Game(id: GameId
                     , gameCreator: Player
                     , players: Seq[Player]
                     , cards: Seq[Card] = Vector.empty[Card]
                     , wordsCount: GameWordsCount = GameWordsCount()
                     , redScore: TeamScore = TeamScore()
                     , blueScore: TeamScore = TeamScore()
                     , moveOrder: MoveOrder = MoveOrder.RedSpymasterMove)

object Game {
  final case class TeamScore(score: Int = 0) extends AnyVal
  final case class GameWordsCount(count: Int = 0) extends AnyVal
}