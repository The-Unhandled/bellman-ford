package forsaken.bellmanford.domain

import forsaken.bellmanford.graph.{Edge, Node}
import io.circe.jawn.decode
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class RateSpec extends AnyFunSuite with RateUtils {

  test(
    "Should create from/to Edge with correct negative weight for a lower than 1 value"
  ) {
    val rate = Rate("YEN", "CHF", 0.006)
    val edge = rate.toEdge
    val expectedEdge = Edge(Node("YEN"), Node("CHF"), 5.115995809754082)

    assert(edge == expectedEdge)
    assertRateEquals(rate, Rate.fromEdge(edge))
  }

  test("Should create from/to Edge with correct values for a ~ 1 value") {
    val rate = Rate("USD", "EUR", 1.11)
    val edge = rate.toEdge
    val expectedEdge = Edge(Node("USD"), Node("EUR"), -0.10436001532424286)

    assert(edge == expectedEdge)
    assertRateEquals(rate, Rate.fromEdge(edge))
  }

  test("Should create from/to Edge with correct values for a > 1 value") {
    val rate = Rate("BTC", "EUR", 55690)
    val edge = rate.toEdge
    val expectedEdge = Edge(Node("BTC"), Node("EUR"), -10.927555876583714)

    assert(edge == expectedEdge)
    assertRateEquals(rate, Rate.fromEdge(edge))
  }

  test("Should correctly decode borgRates.json to RateSet") {
    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    decode[RateSet](ratesJson) match {
      case Right(rateSet) =>
        assert(rateSet.rates.nonEmpty)
        assert(
          rateSet.rates.exists(rate =>
            rate.from == "BTC" && rate.to == "BORG" && rate.value == BigDecimal(
              "0.00000262"
            )
          )
        )
        assert(
          rateSet.rates.exists(rate =>
            rate.from == "DAI" && rate.to == "EUR" && rate.value == BigDecimal(
              "1.09045822"
            )
          )
        )

      case Left(error) =>
        fail(s"Failed to decode rates: $error")
    }
  }

}
