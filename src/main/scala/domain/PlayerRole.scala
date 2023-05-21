package domain

import cats.implicits._
import enumeratum.{Enum, EnumEntry}
import io.circe._

import scala.util.Try

sealed trait PlayerRole extends EnumEntry with Product

object PlayerRole extends Enum[PlayerRole] {
  val values: IndexedSeq[PlayerRole] = findValues

  implicit val playerRoleEncode: Encoder[PlayerRole] = Encoder[String].contramap(_.productPrefix)

  implicit val playerRoleDecode: Decoder[PlayerRole] =
    Decoder
      .decodeString
      .emap(str => Try(PlayerRole.withName(str)).toEither.leftMap(_.getMessage))

  case object Spymaster extends PlayerRole

  case object Operative extends PlayerRole
}