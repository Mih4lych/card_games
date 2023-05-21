package domain

import io.circe._
import io.circe.syntax._

final case class Score(score: Int = 0) extends AnyVal

object Score {
  implicit val teamScoreEncoder: Encoder[Score] = Encoder.instance(_.score.asJson)
  implicit val teamScoreDecoder: Decoder[Score] = Decoder[Int].map(Score(_))
}
