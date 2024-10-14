package forsaken.bellmanford.client

import forsaken.bellmanford.domain.Rate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.*
import sttp.client3.testing.*

import scala.io.Source

class SwissborgRatesClientSpec extends AnyFlatSpec with Matchers {

  ignore should "work with actual call" in {
    val client =
      SwissborgRatesClient("https://api.swissborg.io/v1/challenge/rates")
    val rateSet = client.fetchRates
    println(rateSet)
    rateSet.rates should not be empty
  }

  it should "fetch rates successfully" in {

    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    val url = "https://notgoingtoactuallycall.com/rates"
    // Create the backend stub
    val testingBackend = SttpBackendStub.synchronous.whenAnyRequest
      .thenRespond(Response.ok(ratesJson))

    // Create the client with the stubbed backend
    val client = new SwissborgRatesClient(url, testingBackend)

    // Call the method and verify the result
    val result = client.fetchRates
    result.rates should not be empty
    result.rates should contain(Rate("EUR", "BORG", 0.12814240))
  }
}
