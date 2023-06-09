package service.db

import cats.effect.{Async, Resource}
import cats.syntax.all._
import domain._
import domain.ID._
import slick.jdbc.PostgresProfile.api._
import slickeffect.Transactor

trait PlayerTableDbService[F[_]] {
  def getAllPlayersByGameId(gameId: GameId): F[Seq[Player]]
  def getAllPlayersByTeamId(teamId: TeamId): F[Seq[Player]]
  def getPlayerById(id: PlayerId): F[Option[Player]]
  def insert(player: Player): F[PlayerId]
  def update(player: Player): F[Unit]
  def delete(id: PlayerId): F[Unit]
}
object PlayerTableDbService {
  def apply[F[_]: Async](transactor: Transactor[F]): PlayerTableDbService[F] =
    new PlayerTableDbService[F] {
      override def getAllPlayersByGameId(gameId: GameId): F[Seq[Player]] = transactor.transact(PlayerTable.byGameId(gameId).result)

      override def getAllPlayersByTeamId(teamId: TeamId): F[Seq[Player]] = transactor.transact(PlayerTable.byTeamId(teamId).result)

      override def getPlayerById(id: PlayerId): F[Option[Player]] = transactor.transact(PlayerTable.byId(id).result.headOption)

      override def insert(player: Player): F[PlayerId] = transactor.transact(PlayerTable += player).map(_ => player.id)

      override def update(player: Player): F[Unit] = transactor.transact(PlayerTable.byId(player.id).update(player)).void

      override def delete(id: PlayerId): F[Unit] = transactor.transact(PlayerTable.byId(id).delete).void
    }
}
