package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._

case class Card (id: CardId
                , word: String
                , cardRole: CardRole
                , cardState: CardState = CardState.Closed) {
  def changeState: Card = this.copy(cardState = CardState.Closed)
}

object Card {
  implicit val cardEncoder: Encoder[Card] =
    Encoder.forProduct4("id", "word", "cardRole", "cardState")(s => (s.id, s.word, s.cardRole, s.cardState))
  implicit val cardDecoder: Decoder[Card] =
    Decoder.forProduct4("id", "word", "cardRole", "cardState")(Card.apply)

  def apply(word: String, cardRole: CardRole): Card = {
    Card(CardId(), word, cardRole)
  }
}