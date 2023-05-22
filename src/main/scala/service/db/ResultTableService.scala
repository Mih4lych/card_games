package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait ResultTableService[F[_]] {
  def getResultByGameId(gameId: GameId): F[Option[Result]]
  def getResultById(id: ResultId): F[Option[Result]]
  def insert(result: Result): F[ResultId]
}
object ResultTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): ResultTableService[F] =
    new ResultTableService[F] {
      override def getResultByGameId(gameId: GameId): F[Option[Result]] = transactor.use(_.transact(ResultTable.byGameId(gameId).result.headOption))

      override def getResultById(id: ResultId): F[Option[Result]] = transactor.use(_.transact(ResultTable.byId(id).result.headOption))

      override def insert(result: Result): F[ResultId] = transactor.use(_.transact(ResultTable += result)).map(_ => result.id)
    }
}
