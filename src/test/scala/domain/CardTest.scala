package domain

import domain.ID._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardTest extends AnyWordSpec with Matchers {
  import CardTest._

  "JSON" should {
    "check encoding" in {
      card.asJson.noSpaces must equal(cardRawJson)
    }
    "check decoding" in {
      parse(cardRawJson).getOrElse(Json.Null).as[Card] must be(Right(card))
    }
  }
}

object CardTest {
  private val card = Card(CardId("test"), GameId("test"), "test", CardRole.Agent(TeamColor.Red))
  private val cardRawJson =
    """{"id":"test","gameId":"test","word":"test","cardRole":"RedAgent","cardState":"Hidden"}"""
}
