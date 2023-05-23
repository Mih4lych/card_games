package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait PlayerTableService[F[_]] {
  def getAllPlayersByGameId(gameId: GameId): F[Seq[Player]]
  def getAllPlayersByTeamId(teamId: TeamId): F[Seq[Player]]
  def getPlayerById(id: PlayerId): F[Option[Player]]
  def insert(player: Player): F[Unit]
  def update(id: PlayerId, player: Player): F[Unit]
  def delete(id: PlayerId): F[Unit]
}
object PlayerTableService {
  def apply[F[_]: Async](transactor: Resource[F, Transactor[F]]): PlayerTableService[F] =
    new PlayerTableService[F] {
      override def getAllPlayersByGameId(gameId: GameId): F[Seq[Player]] = transactor.use(_.transact(PlayerTable.byGameId(gameId).result))

      override def getAllPlayersByTeamId(teamId: TeamId): F[Seq[Player]] = transactor.use(_.transact(PlayerTable.byTeamId(teamId).result))

      override def getPlayerById(id: PlayerId): F[Option[Player]] = transactor.use(_.transact(PlayerTable.byId(id).result.headOption))

      override def insert(player: Player): F[Unit] = transactor.use(_.transact(PlayerTable.table += player)).void

      override def update(id: PlayerId, player: Player): F[Unit] = transactor.use(_.transact(PlayerTable.byId(id).update(player))).void

      override def delete(id: PlayerId): F[Unit] = transactor.use(_.transact(PlayerTable.byId(id).delete)).void
    }
}

