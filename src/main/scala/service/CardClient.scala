package service

import cats.effect.kernel.Async
import domain.Game.GameWordsCount
import org.http4s._
import org.http4s.ember.client._
import org.http4s.implicits._
import org.http4s.circe.CirceEntityDecoder._

trait CardClient[F[_]] {
  def getWords(wordsCount: GameWordsCount): F[List[String]]
}

object CardClient {
  def apply[F[_]: Async](): CardClient[F] =
    (wordsCount: GameWordsCount) => {
      val req = Request[F](Method.GET, uri"https://random-word-form.herokuapp.com/random/noun".withQueryParam("count", wordsCount.count))

      EmberClientBuilder
        .default[F]
        .build
        .use(_.expect[List[String]](req))
    }
}
