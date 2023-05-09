package game

import cats.data.EitherT
import cats.effect.std._
import cats.effect.{Async, Ref}
import cats.implicits._
import domain._
import domain.PlayerRole._

trait GameProcess[F[_]] {
  def initiateGame(creator: Player): F[Ref[F, Game]]
  def changeRole(game: Ref[F, Game], playerID: ID, playerRole: PlayerRole): ErrorOrT[F, F[Ref[F, Game]]]
}
object GameProcess {
  def apply[F[_]: Async: Random]: GameProcess[F] =
    new GameProcess[F] {
      override def initiateGame(creator: Player): F[Ref[F, Game]] =
        for {
          game <- Game.createGame(creator)
          ref  <- Ref.of(game)
        } yield ref

      override def changeRole(gameRef: Ref[F, Game], playerID: ID, playerRole: PlayerRole): ErrorOrT[F, F[Ref[F, Game]]] = {
        for {
          _    <- _validateRole(gameRef, playerRole)
          game <- EitherT.liftF {
            gameRef.updateAndGet { game =>
              game.copy(players = updateElementInSeq(game.players)(_.id.equals(playerID))(_.copy(role = playerRole)))
            }
          }
        } yield game
      }

      private def _validateRole(gameRef: Ref[F, Game], playerRole: PlayerRole): ErrorOrT[F, Unit] = {
        playerRole match {
          case role @ RedSpymaster | BlueSpymaster =>
            for {
              game        <- EitherT.liftF(gameRef.get)
              checkResult <- EitherT.fromEither[F](
                if (game.players.exists(p => p.role.equals(role))) GameError.SpymasterExistenceError.asLeft[Unit]
                else ().asRight[GameError])
            } yield checkResult
          case _ => EitherT.fromEither(().asRight[GameError])
        }
      }
    }
}
