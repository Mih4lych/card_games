package domain

import cats.effect.kernel.Sync
import cats.implicits._

case class Card (id: ID
                , word: String
                , cardRole: CardRole
                , cardState: CardState = CardState.Closed) {
  def changeState: Card = this.copy(cardState = CardState.Closed)
}

object Card {
  def create[F[_]: Sync](word: String, cardRole: CardRole): F[Card] = {
    ID().map(cardId => Card(cardId, word, cardRole))
  }
}