package domain

import enumeratum._

sealed trait TeamColor extends EnumEntry

object TeamColor extends Enum[TeamColor] {
  val values: IndexedSeq[TeamColor] = findValues

  case object Blue extends TeamColor

  case object Red extends TeamColor

  case object Gray extends TeamColor

  case object Black extends TeamColor

  case object Empty extends TeamColor
}
