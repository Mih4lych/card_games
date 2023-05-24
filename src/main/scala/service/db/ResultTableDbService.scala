package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait ResultTableDbService[F[_]] {
  def getResultByGameId(gameId: GameId): F[Option[Result]]
  def getResultById(id: ResultId): F[Option[Result]]
  def insert(result: Result): F[ResultId]
}
object ResultTableDbService {
  def apply[F[_]: Async](transactor: Transactor[F]): ResultTableDbService[F] =
    new ResultTableDbService[F] {
      override def getResultByGameId(gameId: GameId): F[Option[Result]] = transactor.transact(ResultTable.byGameId(gameId).result.headOption)

      override def getResultById(id: ResultId): F[Option[Result]] = transactor.transact(ResultTable.byId(id).result.headOption)

      override def insert(result: Result): F[ResultId] = transactor.transact(ResultTable += result).map(_ => result.id)
    }
}
