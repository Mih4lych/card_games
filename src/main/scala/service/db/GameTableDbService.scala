package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait GameTableDbService[F[_]] {
  def getGameById(id: GameId): F[Option[Game]]
  def insert(game: Game): F[GameId]
  def update(game: Game): F[Unit]
}
object GameTableDbService {
  def apply[F[_]: Async](transactor: Transactor[F]): GameTableDbService[F] =
    new GameTableDbService[F] {
      override def getGameById(id: GameId): F[Option[Game]] = transactor.transact(GameTable.byId(id).result.headOption)

      override def insert(game: Game): F[GameId] = transactor.transact(GameTable += game).map(_ => game.id)

      override def update(game: Game): F[Unit] = transactor.transact(GameTable.byId(game.id).update(game)).void
    }
}
