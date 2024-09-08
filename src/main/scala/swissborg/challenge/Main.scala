package swissborg.challenge

import io.circe.jawn.decode

/** @author
  *   Petros Siatos
  */
@main
def main(): Unit = {
  decode[RateSet](ratesJson) match
    case Right(rates) => rates.rates
      .toList
      .sortBy(_.from)
      .foreach(println)
    case Left(error)  => println(error)
}
