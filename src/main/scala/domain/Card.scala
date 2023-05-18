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
  implicit val cardEncoder: Encoder[Card] = deriveEncoder[Card]
  implicit val cardDecoder: Decoder[Card] = deriveDecoder[Card]

  def apply(word: String, cardRole: CardRole): Card = {
    Card(CardId(), word, cardRole)
  }
}