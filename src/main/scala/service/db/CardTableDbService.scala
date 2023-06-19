package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain.Card
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait CardTableDbService[F[_]] {
  def getAllCardsByGameId(gameId: GameId): F[Seq[Card]]
  def getCardById(id: CardId): F[Option[Card]]
  def insert(cards: Seq[Card]): F[Unit]
  def update(card: Card): F[Unit]
}
object CardTableDbService {
  def apply[F[_]: Async](transactor: Transactor[F]): CardTableDbService[F] =
    new CardTableDbService[F] {
      override def getAllCardsByGameId(gameId: GameId): F[Seq[Card]] = transactor.transact(CardTable.byGameId(gameId).result)

      override def getCardById(id: CardId): F[Option[Card]] = transactor.transact(CardTable.byId(id).result.headOption)

      override def insert(cards: Seq[Card]): F[Unit] = transactor.transact(CardTable ++= cards).void

      override def update(card: Card): F[Unit] = transactor.transact(CardTable.byId(card.id).update(card)).void
    }
}
