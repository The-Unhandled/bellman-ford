import io.circe.jawn.decode

/** @author
  *   Petros Siatos
  */
@main
def main(): Unit = {
  decode[RateSet](ratesJson) match
    case Right(rates) => rates.rates
      .toList
      .filterNot(_.isIdentity)
      .sortBy(_.from)
      .foreach(println)
    case Left(error)  => println(error)
}

val ratesJson = s"""{
  "rates": {
    "BORG-BORG": "1.00000000",
    "BORG-BTC": "376833.62765122",
    "BORG-DAI": "6.82463754",
    "BORG-EUR": "7.36616702",
    "BTC-BORG": "0.00000264",
    "BTC-BTC": "1.00000000",
    "BTC-DAI": "0.00001743",
    "BTC-EUR": "0.00001926",
    "DAI-BORG": "0.14556894",
    "DAI-BTC": "55690.62969052",
    "DAI-DAI": "1.00000000",
    "DAI-EUR": "1.11467915",
    "EUR-BORG": "0.13173019",
    "EUR-BTC": "51607.43041574",
    "EUR-DAI": "0.89124770",
    "EUR-EUR": "1.00000000"
  }
}"""
