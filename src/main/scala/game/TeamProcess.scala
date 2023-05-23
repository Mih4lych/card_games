package game

import cats.Functor
import cats.data.EitherT
import cats.effect.Async
import cats.syntax.semigroup._
import domain.GameError._
import domain.ID._
import domain._
import service.db.TeamTableService

trait TeamProcess[F[_]] {
  def createTeam(gameId: GameId, teamColor: TeamColor): F[TeamId]
  def changeScore(teamId: TeamId, points: Score): ErrorOrT[F, Unit]
}

object TeamProcess {
  def apply[F[_]: Async](teamTableService: TeamTableService[F]): TeamProcess[F] =
    new TeamProcess[F] {
      override def createTeam(gameId: GameId, teamColor: TeamColor): F[TeamId] = teamTableService.insert(Team(gameId, teamColor))

      override def changeScore(teamId: TeamId, points: Score): ErrorOrT[F, Unit] = {
        for {
          team <- EitherT.fromOptionF(teamTableService.getTeamById(teamId), TeamNotFoundError)
          _    <- EitherT.liftF(teamTableService.update(team.copy(teamScore = team.teamScore |+| points)))
        } yield ()
      }
    }
}
