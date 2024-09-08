package swissborg.challenge.client

import sttp.client3.*
import sttp.client3.circe.*
import swissborg.challenge.domain.RateSet

class SwissborgRatesClient(url: String, backend: SttpBackend[Identity, Any]):

  def fetchRates: RateSet =
    val request = basicRequest
      .get(uri"$url")
      .response(asJson[RateSet])

    request.send(backend).body match
      case Right(rateSet) => rateSet
      case Left(error) =>
        throw new Exception(s"Failed to fetch rates - ${error.getMessage}")

object SwissborgRatesClient:
  def apply(url: String): SwissborgRatesClient =
    new SwissborgRatesClient(url, HttpClientSyncBackend())
