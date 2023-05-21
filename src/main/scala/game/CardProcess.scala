package game

import cats.syntax.all._
import cats.effect.Sync
import cats.effect.std.Random
import domain.Game.GameWordsCount
import domain.{Card, CardRole, TeamColor}

trait CardProcess[F[_]] {
  def makeCards(words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[Card]]
}

object CardProcess {
  def apply[F[_] : Sync : Random]: CardProcess[F] =
    new CardProcess[F] {
      /*override def makeCards(words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[Card]] = {
        for {
          shuffledCardRoles <- _shuffleRoles(wordsCount, startingTeam)
          cards <- Sync[F].delay(words.zip(shuffledCardRoles).map(Function.tupled(Card.apply)))
        } yield cards
      }

      private def _shuffleRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[CardRole]] = {
        Random[F].shuffleVector(_fillRolesVector(wordsCount, startingTeam))
      }

      private def _fillRolesVector(wordsCount: GameWordsCount, startingTeam: TeamColor): Vector[CardRole] = {
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
    }*/

      override def makeCards(words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[Card]] = ???
    }
}