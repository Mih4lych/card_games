package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain.Card
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait CardTableService[F[_]] {
  def getAllCardByGameId(gameId: GameId): F[Seq[Card]]
  def getCardById(id: CardId): F[Option[Card]]
  def insertCards(cards: Seq[Card]): F[Unit]
  def updateCard(id: CardId, card: Card): F[Unit]
}
object CardTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): CardTableService[F] =
    new CardTableService[F] {
      override def getAllCardByGameId(gameId: GameId): F[Seq[Card]] = transactor.use(_.transact(CardTable.byGameId(gameId).result))

      override def getCardById(id: CardId): F[Option[Card]] = transactor.use(_.transact(CardTable.byId(id).result.headOption))

      override def insertCards(cards: Seq[Card]): F[Unit] = transactor.use(_.transact(CardTable.table ++= cards)).void

      override def updateCard(id: CardId, card: Card): F[Unit] = transactor.use(_.transact(CardTable.byId(id).update(card))).void
    }
}
