package domain

import cats.effect._
case class Game(id: GameId
               , gameCreator: Player
               , cards: Seq[Card]
               , teamRed: Seq[Player]
               , teamBlue: Seq[Player]
               , spectators: Seq[Player]
               , moveOrder: MoveOrder = MoveOrder.RedSpymasterMove)