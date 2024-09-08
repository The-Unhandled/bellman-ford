package swissborg.challenge.client

import sttp.client3._
import sttp.client3.circe._
import io.circe.generic.auto._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import RateSet

class SwissborgRatesClient(url: String) {
  private val backend = HttpURLConnectionBackend()

  def fetchRates(): Future[Either[String, RateSet]] = {
    val request = basicRequest
      .get(uri"$url")
      .response(asJson[RateSet])

    Future {
      request.send(backend).body match {
        case Right(rateSet) => Right(rateSet)
        case Left(error) => Left(error.getMessage)
      }
    }
  }
}