package game

import cats.effect.{Async, Ref}
import cats.implicits._
import domain.ID._
import domain._
import domain.GameError._
import domain.PlayerRole._

trait PlayerProcess {
  def changeTeam(player: Player, newTeam: TeamId): Player
  def changeRole(player: Player, newRole: PlayerRole): Player
  def validateRoleChange(players: Seq[Player], player: Player, newRole: PlayerRole): Either[GameError, Unit]
  def validateOnStart(redTeamPlayers: Seq[Player], blueTeamPlayers: Seq[Player]): Either[GameError, Unit]
}
object PlayerProcess {
  def apply: PlayerProcess =
    new PlayerProcess {
      override def changeTeam(player: Player, newTeam: TeamId): Player = player.copy(teamId = newTeam)

      override def changeRole(player: Player, newRole: PlayerRole): Player = player.copy(role = newRole)

      override def validateRoleChange(players: Seq[Player], player: Player, newRole: PlayerRole): Either[GameError, Unit] = {
        newRole match {
          case Spymaster =>
            if (players.exists(_.role.equals(Spymaster))) SpymasterAlreadyExistsError.asLeft
            else ().asRight
          case Operative =>
            ().asRight
        }
      }

      override def validateOnStart(redTeamPlayers: Seq[Player], blueTeamPlayers: Seq[Player]): Either[GameError, Unit] = {
        def validatePlayers(players: Seq[Player]): Either[GameError, Unit] = {
          if (!players.exists(_.role.equals(Spymaster))) TeamWithoutSpymasterError.asLeft
          else if (!players.exists(_.role.equals(Operative))) TeamWithoutOperativesError.asLeft
          else ().asRight
        }

        for {
          _ <- validatePlayers(redTeamPlayers)
          _ <- validatePlayers(blueTeamPlayers)
        } yield ()
      }
    }
}
