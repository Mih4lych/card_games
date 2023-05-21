package domain

import domain.ID._
import io.circe._
import io.circe.generic.semiauto._

case class Card (id: CardId
                , gameId: GameId
                , word: String
                , cardRole: CardRole
                , cardState: CardState = CardState.Hidden) {
  def revealCard: Card = this.copy(cardState = CardState.Revealed)
}

object Card {
  implicit val cardEncoder: Encoder[Card] = deriveEncoder[Card]
  implicit val cardDecoder: Decoder[Card] = deriveDecoder[Card]

  def apply(gameId: GameId, word: String, cardRole: CardRole): Card = {
    Card(CardId(), gameId, word, cardRole)
  }
}