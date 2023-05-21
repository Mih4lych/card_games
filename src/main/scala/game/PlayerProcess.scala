package game

import cats.effect.{Async, Ref}
import domain.ID.{CardId, TeamId}
import domain.{Game, Player, PlayerRole}
import util.ErrorOrT

trait PlayerProcess[F[_]] {
  def playCard(gameRef: Ref[F, Game], cardId: CardId): ErrorOrT[F, Unit]
}
object PlayerProcess {
  def apply[F[_]: Async]: PlayerProcess[F] =
    new PlayerProcess[F] {
      override def playCard(gameRef: Ref[F, Game], cardId: CardId): ErrorOrT[F, Unit] = ???

      def changeTeam(player: Player, teamId: TeamId) = ???
    }
}
