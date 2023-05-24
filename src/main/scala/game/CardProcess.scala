package game

import cats.data.EitherT
import cats.effect._
import cats.effect.std.Random
import cats.syntax.all._
import domain._
import domain.Game._
import domain.GameError._
import domain.ID._
import service.CardClient
import service.db.CardTableDbService

trait CardProcess[F[_]] {
  def makeCards(game: Game): F[Unit]
  def revealCard(cardId: Card): F[Unit]
  def getCardById(cardId: CardId): ErrorOrT[F, Card]
}

object CardProcess {
  def apply[F[_]: Async: Random](cardTableService: CardTableDbService[F], cardClient: CardClient[F]): CardProcess[F] =
    new CardProcess[F] {
      override def makeCards(game: Game): F[Unit] = {
        for {
          words             <- cardClient.getWords(game.wordsCount)
          shuffledCardRoles <- _shuffleRoles(game.getMaxScore.score, TeamColor.Red)
          cards             <-
            Sync[F]
              .delay(words.zip(shuffledCardRoles).map {
                case (word, role) => Card(game.id, word, role)
              })
          _                 <- cardTableService.insert(cards)
        } yield ()
      }

      private def _shuffleRoles(cardCountPerRole: Int, startingTeam: TeamColor): F[List[CardRole]] = {
        Random[F].shuffleList(_fillRoles(cardCountPerRole, startingTeam))
      }

      private def _fillRoles(cardCountPerRole: Int, startingTeam: TeamColor): List[CardRole] = {
        List.fill(cardCountPerRole)(CardRole.Agent(TeamColor.Red)) ++
          List.fill(cardCountPerRole)(CardRole.Agent(TeamColor.Blue)) ++
          List.fill(cardCountPerRole)(CardRole.Innocent) ++
          List(CardRole.Assassin) ++ {
          startingTeam match {
            case TeamColor.Blue => List(CardRole.Agent(TeamColor.Red))
            case TeamColor.Red  => List(CardRole.Agent(TeamColor.Blue))
          }
        }
      }

      override def revealCard(card: Card): F[Unit] = cardTableService.update(card.revealCard)

      override def getCardById(cardId: CardId): ErrorOrT[F, Card] = EitherT.fromOptionF(cardTableService.getCardById(cardId), NotFoundError("Card"))
    }
}