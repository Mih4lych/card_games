package domain

import io.circe._
import io.circe.generic.semiauto._

case class Clue(clue: String
               , relatingWordsCount: Int)

object Clue {
  implicit val clueEncoder: Encoder[Clue] = deriveEncoder[Clue]
  implicit val clueDecoder: Decoder[Clue] = deriveDecoder[Clue]
}
