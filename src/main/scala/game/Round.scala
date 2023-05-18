package game

import cats.effect.kernel.Sync
import domain.Game
import domain.ID._

object Round {
  sealed trait Command

  case class PlayCardCommand(game: Game, playerId: PlayerId, cardId: CardId) extends Command
  case class StartGameCommand(game: Game) extends Command
  case class EndRoundCommand(game: Game, playerId: PlayerId) extends Command
  case class GiveClueCommand(game: Game, playerId: PlayerId, clue: String, relatingNumber: Int) extends Command

  sealed trait Event

  case class PostClueEvent(game: Game, playerId: PlayerId) extends Event
  case class OpenedCardEvent(game: Game, playerId: PlayerId, cardId: CardId) extends Event
  case class ChangeRoundEvent(game: Game, playerId: PlayerId) extends Event
  case class ChangeGameStatusEvent(game: Game) extends Event

  def processRound[F[_]: Sync](command: Command): F[Event] = {
    command match {
      case StartGameCommand(game) => ???
      case PlayCardCommand(game, playerId, cardId) => ???
      case EndRoundCommand(game, playerId) => ???
      case GiveClueCommand(game, playerId, clue, relatingNumber) => ???
    }
  }
}