package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait TeamTableService[F[_]] {
  def getAllTeamsByGameId(gameId: GameId): F[Seq[Team]]
  def getTeamById(id: TeamId): F[Option[Team]]
  def insert(team: Team): F[TeamId]
  def update(team: Team): F[Unit]
}
object TeamTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): TeamTableService[F] =
    new TeamTableService[F] {
      override def getAllTeamsByGameId(gameId: GameId): F[Seq[Team]] = transactor.use(_.transact(TeamTable.byGameId(gameId).result))

      override def getTeamById(id: TeamId): F[Option[Team]] = transactor.use(_.transact(TeamTable.byId(id).result.headOption))

      override def insert(team: Team): F[TeamId] = transactor.use(_.transact(TeamTable += team)).map(_ => team.id)

      override def update(team: Team): F[Unit] = transactor.use(_.transact(TeamTable.byId(team.id).update(team))).void
    }
}
