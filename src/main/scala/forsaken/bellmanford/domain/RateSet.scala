package forsaken.bellmanford.domain

import cats.syntax.traverse.*
import io.circe.{Decoder, DecodingFailure, HCursor}

case class RateSet(rates: Set[Rate])

object RateSet:
  implicit val decodeRateSet: Decoder[RateSet] = (c: HCursor) =>
    for
      rateKeys <- c.downField("rates").keys match
        case Some(rates) => Right(rates)
        case None        => Left(DecodingFailure("Missing rates", c.history))
      rates <- rateKeys.toList.traverse { pair =>
        pair.split("-") match
          case Array(from, to) =>
            for price <- c.downField("rates").downField(pair).as[Double]
            yield Rate(from, to, price)
          case _ => Left(DecodingFailure("Invalid pair", c.history))
      }
    yield RateSet(rates.toSet)
