package game

import cats.implicits._
import domain._
import domain.ID._
import domain.GameError._
import util.updateElementInSeq

trait TeamProcess {
  def addPlayer(team: Team, player: Player): Either[GameError, Team]
  def changeRole(team: Team, playerId: PlayerId, role: PlayerRole): Either[GameError, Team]
  def removePlayer(team: Team, playerId: PlayerId): Team
}

object TeamProcess {
  def apply(): TeamProcess =
    new TeamProcess {
      override def addPlayer(team: Team, player: Player): Either[GameError, Team] = {
        for {
          _ <- _validateRoleChange(team, player.id, PlayerRole.Operative)
          team <- team.copy(players = player +: team.players).asRight[GameError]
        } yield team
      }

      override def changeRole(team: Team, playerId: PlayerId, role: PlayerRole): Either[GameError, Team] = {
        for {
          _ <- _validateRoleChange(team, playerId, PlayerRole.Operative)
          team <-
            team.copy(players = updateElementInSeq(team.players)(_.id.equals(playerId))(_.changeRole(role))
              , hasSpymaster = role match {
                case PlayerRole.Spymaster => true
                case PlayerRole.Operative => team.players.find(_.id.equals(playerId)).forall(!_.role.equals(PlayerRole.Spymaster))
              }).asRight
        } yield team
      }

      override def removePlayer(team: Team, playerId: PlayerId): Team = {
        val (playerToRemove, restPlayers) = team.players.partition(_.id.equals(playerId))
        team.copy(players = restPlayers, hasSpymaster = playerToRemove.forall(!_.role.equals(PlayerRole.Spymaster)))
      }

      private def _validateRoleChange(team: Team, playerId: PlayerId, role: PlayerRole): Either[GameError, Unit] = {
        role match {
          case PlayerRole.Spymaster =>
            if (team.hasSpymaster)
              SpymasterAlreadyInTeamError.asLeft
            else
              ().asRight
          case PlayerRole.Operative =>
            if (team.players.exists(_.id == playerId))
              PlayerAlreadyInTeamError.asLeft
            else
              ().asRight
        }
      }

      def validateTeam(team: Team): Either[GameError, Unit] = {
        if (!team.hasSpymaster) TeamWithoutSpymasterError.asLeft
        else if (team.players.forall(_.role.equals(PlayerRole.Spymaster))) TeamWithoutOperativeError.asLeft
        else ().asRight
      }
    }
}
