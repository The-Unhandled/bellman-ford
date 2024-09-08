package swissborg.challenge.client

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.*
import sttp.client3.testing.*
import io.circe.generic.auto.*
import swissborg.challenge.domain.{Rate, RateSet}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SwissborgRatesClientSpec extends AnyFlatSpec with Matchers {

  "SwissborgRatesClient" should "fetch rates successfully" in {

    val rates = Set(
      Rate("EUR", "USD", 1.1),
      Rate("USD", "GBP", 0.75),
      Rate("GBP", "EUR", 1.2)
    )
    val rateSet = RateSet(rates)

    // Create the backend stub
    val backendStub = SttpBackendStub.synchronous
      .whenRequestMatches(_ => true)
      .thenRespond(rateSet)

    // Create the client with the stubbed backend
    val client = new SwissborgRatesClient("https://notgoingtoactuallycall.com/rates", backendStub)

    // Call the method and verify the result
    val result = client.fetchRates
    result shouldBe rateSet
  }

  it should "return an error when fetching rates fails" in {
    // Create the backend stub
    val backendStub = SttpBackendStub.synchronous
      .whenRequestMatches(_ => true)
      .thenRespondServerError()

    // Create the client with the stubbed backend
    val client = new SwissborgRatesClient("https://api.swissborg.io/v1/challenge/rates", backendStub)

    // Call the method and verify the exception
    an[Exception] should be thrownBy client.fetchRates
  }
}