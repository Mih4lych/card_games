package domain

import java.util.UUID

UUID.randomUUID()

case class Game(id: UUID
               , cards: Map[UUID, Card] //переделать в seq
               , teamRed: Map[UUID, Player]
               , teamBlue: Map[UUID, Player]
               , spectators: Map[UUID, Player]
               , gameCreator: Player
               , gameState: GameState)
