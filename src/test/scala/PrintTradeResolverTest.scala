import org.scalatest.funsuite.AnyFunSuite

class PrintTradeResolverTest extends AnyFunSuite {

  test("PrintTradeResolver should correctly resolve trades with 3 rates") {
    val rates = List(
      Rate("USD", "EUR", 0.9),
      Rate("EUR", "GBP", 0.85),
      Rate("GBP", "JPY", 194.0),
      Rate("JPY", "USD", 0.008)
    )

    val startingValue = 100
    val profitPercentage = PrintTradeResolver.resolve("USD", rates, startingValue)

    assert(profitPercentage == 0.1872800)
  }
}