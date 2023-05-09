package domain

import cats.effect.kernel.Sync
import cats.implicits._
import com.sun.tools.javac.code.TypeTag
import enumeratum.{Enum, EnumEntry}

import java.util.UUID

case class ID(id: String) extends AnyVal

object ID {
  def apply[F[_]: Sync](): F[ID] =
    Sync[F].delay(UUID.randomUUID().toString).map(ID(_))
}