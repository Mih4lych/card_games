package game

import cats.syntax.all._
import cats.effect._
import cats.effect.std.Random
import domain._
import domain.Game._

trait CardProcess[F[_]] {
  def makeCards(gameRef: Ref[F, Game], words: Vector[String]): F[Vector[Card]]
}

object CardProcess {
  def apply[F[_] : Sync : Random]: CardProcess[F] =
    new CardProcess[F] {
      override def makeCards(gameRef: Ref[F, Game], words: Vector[String]): F[Vector[Card]] = {
        for {
          game              <- gameRef.get
          shuffledCardRoles <- _shuffleRoles(game.wordsCount, TeamColor.Red)
          cards             <-
            Sync[F]
              .delay(words.zip(shuffledCardRoles).map {
                case (word, role) => Card(game.id, word, role)
              })
        } yield cards
      }

      private def _shuffleRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[CardRole]] = {
        Random[F].shuffleVector(_fillRoles(wordsCount, startingTeam))
      }

      private def _fillRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): Vector[CardRole] = {
        val countPerRole = (wordsCount.count - 1) / 3

        Vector.fill(countPerRole)(CardRole.Agent(TeamColor.Red)) ++
          Vector.fill(countPerRole)(CardRole.Agent(TeamColor.Blue)) ++
          Vector.fill(countPerRole)(CardRole.Innocent) ++
          Vector(CardRole.Assassin) ++ {
          startingTeam match {
            case TeamColor.Blue => Vector(CardRole.Agent(TeamColor.Red))
            case TeamColor.Red => Vector(CardRole.Agent(TeamColor.Blue))
          }
        }
      }
    }
}