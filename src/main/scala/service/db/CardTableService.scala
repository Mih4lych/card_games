package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain.Card
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait CardTableService[F[_]] {
  def getAllCardsByGameId(gameId: GameId): F[Seq[Card]]
  def getCardById(id: CardId): F[Option[Card]]
  def insert(cards: Seq[Card]): F[Unit]
  def update(card: Card): F[Unit]
}
object CardTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): CardTableService[F] =
    new CardTableService[F] {
      override def getAllCardsByGameId(gameId: GameId): F[Seq[Card]] = transactor.use(_.transact(CardTable.byGameId(gameId).result))

      override def getCardById(id: CardId): F[Option[Card]] = transactor.use(_.transact(CardTable.byId(id).result.headOption))

      override def insert(cards: Seq[Card]): F[Unit] = transactor.use(_.transact(CardTable ++= cards)).void

      override def update(card: Card): F[Unit] = transactor.use(_.transact(CardTable.byId(card.id).update(card))).void
    }
}
