package game

import cats.effect.{Async, Ref}
import domain.Game._
import domain._
import util._

trait GameProcess[F[_]] {
  //def createGame(creator: Player): F[Ref[F, Game]]
  def startGame(gameRef: Ref[F, Game], words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): ErrorOrT[F, Unit]
  def changeScore(gameRef: Ref[F, Game], teamColor: TeamColor): F[Unit]
}
object GameProcess {
  def apply[F[_] : Async](cardProcess: CardProcess[F], teamProcess: TeamProcess): GameProcess[F] =
    new GameProcess[F] {
      /*
      override def startGame(gameRef: Ref[F, Game], words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): ErrorOrT[F, Unit] = {
        for {
          game  <- EitherT.liftF(gameRef.get)
          //_     <- EitherT.fromEither(_validateTeams(game))
          cards <- EitherT.liftF(cardProcess.makeCards(words, wordsCount, startingTeam))
          _     <- EitherT.liftF(gameRef.update(game => game.copy(cards = cards, gameState = GameState.InProgress, moveOrder = MoveOrder.SpymasterMove(startingTeam))))
        } yield ()
      }

      /*private def _validateTeams(game: Game): Either[GameError, Unit] = {
        for {
          _ <- teamProcess.validateTeam(game.redTeam)
          _ <- teamProcess.validateTeam(game.blueTeam)
        } yield ()
      }*/

      override def changeScore(gameRef: Ref[F, Game], teamColor: TeamColor): F[Unit] = {
        gameRef.update { game =>
          teamColor match {
            case TeamColor.Blue =>
              game.copy(blueScore = TeamScore(game.blueScore.score + 1))
            case TeamColor.Red =>
              game.copy(redScore = TeamScore(game.redScore.score + 1))
          }
        }
      }
    }*/

      override def startGame(gameRef: Ref[F, Game], words: Vector[String], wordsCount: GameWordsCount, startingTeam: TeamColor): ErrorOrT[F, Unit] = ???

      override def changeScore(gameRef: Ref[F, Game], teamColor: TeamColor): F[Unit] = ???
    }
}
