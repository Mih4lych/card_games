package game

import cats.data.EitherT
import cats.effect.Async
import cats.syntax.all._
import domain.GameError._
import domain.ID._
import domain._
import service.db.TeamTableDbService

trait TeamProcess[F[_]] {
  def createTeam(gameId: GameId, teamColor: TeamColor): F[TeamId]
  def changeScore(team: Team, points: Score): F[Team]
  def getTeamsIdByGame(gameId: GameId): F[Seq[TeamId]]
  def getTeamById(teamId: TeamId): ErrorOrT[F, Team]
  def getEnemyTeam(gameId: GameId, teamId: TeamId): ErrorOrT[F, Team]
}

object TeamProcess {
  def apply[F[_] : Async](teamTableService: TeamTableDbService[F]): TeamProcess[F] =
    new TeamProcess[F] {
      override def createTeam(gameId: GameId, teamColor: TeamColor): F[TeamId] = teamTableService.insert(Team(gameId, teamColor))

      override def changeScore(team: Team, points: Score): F[Team] = teamTableService.update(team.copy(teamScore = team.teamScore + points))

      override def getTeamsIdByGame(gameId: GameId): F[Seq[TeamId]] = teamTableService.getAllTeamsByGameId(gameId).map(_.map(_.id))

      override def getTeamById(teamId: TeamId): ErrorOrT[F, Team] = EitherT.fromOptionF(teamTableService.getTeamById(teamId), NotFoundError("Team"))

      override def getEnemyTeam(gameId: GameId, teamId: TeamId): ErrorOrT[F, Team] = EitherT.fromOptionF(teamTableService.getAllTeamsByGameId(gameId).map(_.filterNot(_.id.equals(teamId)).headOption), NotFoundError("Team"))
    }
}
