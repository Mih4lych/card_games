package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait GameTableService[F[_]] {
  def getGameByCreator(creator: PlayerId): F[Option[Game]]
  def getGameById(id: GameId): F[Option[Game]]
  def insert(game: Game): F[GameId]
  def update(game: Game): F[Unit]
}
object GameTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): GameTableService[F] =
    new GameTableService[F] {
      override def getGameByCreator(creator: PlayerId): F[Option[Game]] = transactor.use(_.transact(GameTable.byCreatorId(creator).result.headOption))

      override def getGameById(id: GameId): F[Option[Game]] = transactor.use(_.transact(GameTable.byId(id).result.headOption))

      override def insert(game: Game): F[GameId] = transactor.use(_.transact(GameTable += game)).map(_ => game.id)

      override def update(game: Game): F[Unit] = transactor.use(_.transact(GameTable.byId(game.id).update(game))).void
    }
}
