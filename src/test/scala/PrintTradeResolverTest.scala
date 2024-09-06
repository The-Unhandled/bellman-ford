import org.scalatest.funsuite.AnyFunSuite

class PrintTradeResolverTest extends AnyFunSuite {

  test("PrintTradeResolver should correctly resolve trades with 3 rates") {
    val rates = List(
      Rate("USD", "EUR", 0.9),
      Rate("EUR", "GBP", 0.85),
      Rate("GBP", "JPY", 188.0),
      Rate("JPY", "USD", 0.007)
    )

    val startingValue = BigDecimal(100)
    val finalValue = PrintTradeResolver.resolve(rates, startingValue)

    assert(finalValue == startingValue * 0.9 * 0.85 * 188.0 * 0.007)
  }
}