package game

import cats.data.EitherT
import cats.syntax.all._
import cats.effect.kernel.Async
import cats.effect.std.Random
import domain.Game.GameWordsCount
import domain.{Card, CardRole, GameError, ID, TeamColor}
import fs2.Stream
import io.circe.Json
import org.http4s.Status.Ok
import org.http4s._
import org.http4s.ember.client._
import org.http4s.implicits._
import org.typelevel.jawn.Facade
import org.typelevel.jawn.fs2._

trait CardProcess[F[_]] {
  def makeCards(wordsCount: GameWordsCount, startingTeam: TeamColor): EitherT[F, GameError, Vector[Card]]
}

object CardProcess {
  def apply[F[_]: Async: Random]: CardProcess[F] =
    new CardProcess[F] {
      override def makeCards(wordsCount: GameWordsCount, startingTeam: TeamColor): EitherT[F, GameError, Vector[Card]] = {
        for {
          words <- EitherT(_getWords.take(wordsCount.count).compile.toVector.map(_.sequence))
          shuffledCardRoles <- EitherT.liftF(_shuffleRoles(wordsCount, startingTeam))
          cards <- EitherT.liftF(words.zip(shuffledCardRoles).traverse(Function.tupled(Card.create[F])))
        } yield cards
      }

      private def _getWords: Stream[F, Either[GameError, String]] = {
        val req = Request[F](Method.GET, uri"https://random-word-form.herokuapp.com/random/noun")
        implicit val f: Facade[Json] = new io.circe.jawn.CirceSupportParser(None, false).facade

        for {
          client <- Stream.resource(EmberClientBuilder.default[F].build)
          resp <- client.stream(req).repeat
          word <- resp.status match {
            case Ok => resp.body.chunks.parseJsonStream.map(_.as[List[String]].bimap(_ => GameError.WordParseError, _.head))
            case _ => Stream(GameError.WordServiceConnectionError.asLeft[String]).covary[F]
          }
        } yield word
      }

      private def _shuffleRoles(wordsCount: GameWordsCount, startingTeam: TeamColor): F[Vector[CardRole]] = {
        Random[F].shuffleVector(_fillRolesVector(wordsCount, startingTeam))
      }

      private def _fillRolesVector(wordsCount: GameWordsCount, startingTeam: TeamColor): Vector[CardRole] = {
        val countPerRole = (wordsCount.count - 1) / 3
        Vector.fill(countPerRole)(CardRole.RedAgent) ++
          Vector.fill(countPerRole)(CardRole.BlueAgent) ++
          Vector.fill(countPerRole)(CardRole.Innocent) ++
          Vector(CardRole.Assassin) ++ {
          startingTeam match {
            case TeamColor.Blue => Vector(CardRole.BlueAgent)
            case TeamColor.Red => Vector(CardRole.RedAgent)
            case _ => Vector.empty[CardRole]
          }
        }
      }
    }
}