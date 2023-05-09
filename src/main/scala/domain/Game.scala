package domain

import cats.effect.Sync
import cats.syntax.all._
import domain.Game._

final case class Game(id: ID
                     , gameCreator: Player
                     , players: Seq[Player]
                     , cards: Seq[Card] = Vector.empty[Card]
                     , wordsCount: GameWordsCount = GameWordsCount()
                     , redScore: TeamScore = TeamScore()
                     , blueScore: TeamScore = TeamScore()
                     , moveOrder: MoveOrder = MoveOrder.RedSpymasterMove) {

  def connectPlayer(player: Player): Game = this.copy(players = player +: this.players)
  def disconnectPlayer(player: Player): Game = this.copy(players = this.players.filterNot(_.equals(player)))
  def disconnectPlayerByID(playerID: ID): Game = this.copy(players = this.players.filterNot(_.id.equals(playerID)))
  def setWordCount(count: Int): Game = this.copy(wordsCount = GameWordsCount(count))
}

object Game {
  final case class TeamScore(score: Int = 0) extends AnyVal
  final case class GameWordsCount(count: Int = 0) extends AnyVal

  def createGame[F[_]: Sync](creator: Player): F[Game] = {
    ID()
      .map { gameId =>
        Game(id = gameId, gameCreator = creator, players = Vector(creator))
      }
  }
}