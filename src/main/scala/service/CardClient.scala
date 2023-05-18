package service

import cats.effect.kernel.Async
import org.http4s._
import org.http4s.ember.client._
import org.http4s.implicits._
import org.http4s.circe.CirceEntityDecoder._

trait CardClient[F[_]] {
  def getWords(wordsCount: Int): F[Vector[String]]
}
object CardClient {
  def apply[F[_]: Async](): CardClient[F] =
    (wordsCount: Int) => {
      val req = Request[F](Method.GET, uri"https://random-word-form.herokuapp.com/random/noun".withQueryParam("count", wordsCount))

      EmberClientBuilder
        .default[F]
        .build
        .use(_.expect[Vector[String]](req))
    }
}
