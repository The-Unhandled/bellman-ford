package forsaken.bellmanford.domain

import org.scalatest.funsuite.AnyFunSuite

import scala.math.BigDecimal.RoundingMode.HALF_UP

class PrintTradeResolverSpec extends AnyFunSuite {

  test("PrintTradeResolver should correctly resolve trades with 3 rates") {
    val rates = List(
      Rate("USD", "EUR", 0.9),
      Rate("EUR", "GBP", 0.85),
      Rate("GBP", "JPY", 194.0),
      Rate("JPY", "USD", 0.008)
    )

    val startingValue = 100
    val profitPercentage =
      PrintTradeResolver.resolve("USD", rates, startingValue)

    assert(BigDecimal(profitPercentage).setScale(2, HALF_UP) == 18.73)
  }
}
