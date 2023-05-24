package game

import cats.data.EitherT
import cats.syntax.all._
import cats.effect._
import cats.effect.std.Random
import domain._
import domain.Game._
import domain.GameError._
import domain.ID.CardId
import game.Round.Event
import service.db.CardTableService

trait CardProcess[F[_]] {
  def makeCards(gameRef: Ref[F, Game], words: List[String]): F[Unit]
  def playCard(cardId: CardId): ErrorOrT[F, Unit]
}

object CardProcess {
  def apply[F[_]: Async: Random](cardTableService: CardTableService[F]): CardProcess[F] =
    new CardProcess[F] {
      override def makeCards(gameRef: Ref[F, Game], words: List[String]): F[Unit] = {
        for {
          game              <- gameRef.get
          shuffledCardRoles <- _shuffleRoles(game.wordsCount, TeamColor.Red)
          cards             <-
            Sync[F]
              .delay(words.zip(shuffledCardRoles).map {
                case (word, role) => Card(game.id, word, role)
              })
          _                 <- cardTableService.insert(cards)
        } yield ()
      }

      private def _shuffleRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): F[List[CardRole]] = {
        Random[F].shuffleList(_fillRoles(wordsCount, startingTeam))
      }

      private def _fillRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): List[CardRole] = {
        val countPerRole = (wordsCount.count - 1) / 3

        List.fill(countPerRole)(CardRole.Agent(TeamColor.Red)) ++
          List.fill(countPerRole)(CardRole.Agent(TeamColor.Blue)) ++
          List.fill(countPerRole)(CardRole.Innocent) ++
          List(CardRole.Assassin) ++ {
          startingTeam match {
            case TeamColor.Blue => List(CardRole.Agent(TeamColor.Red))
            case TeamColor.Red  => List(CardRole.Agent(TeamColor.Blue))
          }
        }
      }

      override def playCard(cardId: CardId): ErrorOrT[F, Unit] = {
        for {
          card <- EitherT.fromOptionF(cardTableService.getCardById(cardId), CardNotFoundError)
          _    <- EitherT.liftF(cardTableService.update(card.revealCard))
        } yield ()
      }
    }
}