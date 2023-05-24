package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait TeamTableDbService[F[_]] {
  def getAllTeamsByGameId(gameId: GameId): F[Seq[Team]]
  def getTeamById(id: TeamId): F[Option[Team]]
  def insert(team: Team): F[TeamId]
  def update(team: Team): F[Team]
}
object TeamTableDbService {
  def apply[F[_]: Async](transactor: Transactor[F]): TeamTableDbService[F] =
    new TeamTableDbService[F] {
      override def getAllTeamsByGameId(gameId: GameId): F[Seq[Team]] = transactor.transact(TeamTable.byGameId(gameId).result)

      override def getTeamById(id: TeamId): F[Option[Team]] = transactor.transact(TeamTable.byId(id).result.headOption)

      override def insert(team: Team): F[TeamId] = transactor.transact(TeamTable += team).map(_ => team.id)

      override def update(team: Team): F[Team] = transactor.transact(TeamTable.byId(team.id).update(team)).map(_ => team)
    }
}
