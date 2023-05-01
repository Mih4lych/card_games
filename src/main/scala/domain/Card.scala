package domain

case class Card (word: String
                , cardRole: CardRole
                , cardState: CardState = CardState.Closed)
