package game

import cats.data.EitherT
import cats.effect.kernel.Async
import domain.GameError._
import domain.ID._
import domain._
import service.db.ResultTableDbService

trait ResultProcess[F[_]] {
  def createResult(gameId: GameId, winningTeam: TeamId, blueTeamScore: Score, redTeamScore: Score): F[ResultId]
  def getResultById(resultId: ResultId): ErrorOrT[F, Result]
  def getResultByGameId(gameId: GameId): ErrorOrT[F, Result]
}

object ResultProcess {
  def apply[F[_]: Async](resultTableDbService: ResultTableDbService[F]): ResultProcess[F] = {
    new ResultProcess[F] {
      override def createResult(gameId: GameId, winningTeam: TeamId, blueTeamScore: Score, redTeamScore: Score): F[ResultId] =
        resultTableDbService.insert(Result(gameId, winningTeam, blueTeamScore, redTeamScore))

      override def getResultById(resultId: ResultId): ErrorOrT[F, Result] = EitherT.fromOptionF(resultTableDbService.getResultById(resultId), NotFoundError("Result"))

      override def getResultByGameId(gameId: GameId): ErrorOrT[F, Result] = EitherT.fromOptionF(resultTableDbService.getResultByGameId(gameId), NotFoundError("Result"))
    }
  }
}
