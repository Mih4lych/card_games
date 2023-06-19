package domain

import cats.kernel.Semigroup
import cats.implicits._
import io.circe._
import io.circe.syntax._

final case class Score(score: Int = 0) extends AnyVal {
  def +(that: Score): Score = Score(this.score + that.score)
  def -(that: Score): Score = Score(this.score - that.score)
}

object Score {
  implicit val teamScoreEncoder: Encoder[Score] = Encoder.instance(_.score.asJson)
  implicit val teamScoreDecoder: Decoder[Score] = Decoder[Int].map(Score(_))
}
