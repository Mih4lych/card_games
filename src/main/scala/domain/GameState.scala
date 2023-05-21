package domain

import cats.implicits._
import enumeratum.{Enum, EnumEntry}
import io.circe._

import scala.util.Try

sealed trait GameState extends EnumEntry with Product

object GameState extends Enum[GameState] {
  val values: IndexedSeq[GameState] = findValues

  implicit val gameStateEncode: Encoder[GameState] = Encoder[String].contramap(_.productPrefix)

  implicit val gameStateDecode: Decoder[GameState] =
    Decoder
      .decodeString
      .emap(str => Try(GameState.withName(str)).toEither.leftMap(_.getMessage))

  case object WaitingPayers extends GameState

  case object InProgress extends GameState

  case object Finished extends GameState
}