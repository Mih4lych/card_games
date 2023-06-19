package service

import io.chrisdavenport.mules._
import cats.effect._
import cats.effect.std.MapRef
import cats.syntax.all._
import domain.Game
import domain.ID.GameId
import io.chrisdavenport.mules.MemoryCache.MemoryCacheItem
import service.db.GameTableDbService

trait GameCache[F[_]] {
  def insert(game: Game): F[GameId]
  def update(game: Game): F[Unit]
  def get(gameId: GameId): F[Option[Game]]
}

object GameCache {
  def apply[F[_]: Async](gameTableService: GameTableDbService[F], cache: MemoryCache[F, GameId, Game]): GameCache[F] = {
    /*
      MapRef.ofSingleImmutableMap(Map.empty[GameId, MemoryCacheItem[Game]])
      .map(MemoryCache.ofMapRef(_, None))
      .map { cache =>
     */
    new GameCache[F] {
      override def insert(game: Game): F[GameId] = {
        for {
          gameId <- gameTableService.insert(game)
          _      <- cache.insert(game.id, game)
        } yield gameId
      }

      override def update(game: Game): F[Unit] = {
        for {
          _ <- gameTableService.update(game)
          _ <- cache.insert(game.id, game)
        } yield ()
      }

      override def get(gameId: GameId): F[Option[Game]] = {
        for {
          gameFromCache      <- cache.lookup(gameId)
          finalGameGetResult <-
            if (gameFromCache.isDefined) Async[F].delay(gameFromCache)
            else gameTableService.getGameById(gameId).flatTap(_.fold(().pure)(game => cache.insert(gameId, game)))
        } yield finalGameGetResult
      }
    }
  }
}
