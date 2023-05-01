package domain

import cats.effect.kernel.Sync
import cats.implicits._

case class Card (id: CardId
                , word: String
                , cardRole: CardRole
                , cardState: CardState = CardState.Closed)

object Card {
  def create[F[_]: Sync](word: String, cardRole: CardRole): F[Card] = {
    ID[F, CardId]
      .map(cardId => Card(cardId, word, cardRole))
  }
}