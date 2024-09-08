import io.circe.jawn.decode
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class RateSpec extends AnyFunSuite {

  def assertRateEquals(expected: Rate, actual: Rate, tolerance: BigDecimal): Unit = {
    assert(expected.from == actual.from)
    assert(expected.to == actual.to)
    assert((expected.value - actual.value).abs <= tolerance)
  }


  test("Should correctly decode borgRates.json to RateSet") {
    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    decode[RateSet](ratesJson) match {
      case Right(rateSet) =>
        assert(rateSet.rates.nonEmpty)
        assert(rateSet.rates.exists(rate => rate.from == "BTC" && rate.to == "BORG" && rate.value == BigDecimal("371065.07198622")))
        assert(rateSet.rates.exists(rate => rate.from == "DAI" && rate.to == "EUR" && rate.value == BigDecimal("0.88974065")))

      case Left(error) =>
        fail(s"Failed to decode rates: $error")
    }
  }

}