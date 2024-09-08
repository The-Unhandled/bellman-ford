import cats.instances.list.*
import cats.syntax.traverse.*
import io.circe.{Decoder, DecodingFailure, HCursor}

case class RateSet(rates : Set[Rate])

object RateSet:
  implicit val decodeRateSet: Decoder[RateSet] = (c: HCursor) => for
    rateKeys <- c.downField("rates").keys match
      case Some(rates) => Right(rates)
      case None => Left(DecodingFailure("Missing rates", c.history))
    rates <- rateKeys.toList.traverse { pair =>
      pair.split("-") match
        case Array(to, from) =>
          for
            price <- c.downField("rates").downField(pair).as[BigDecimal]
            priceWithPrecision2 = price.setScale(2, BigDecimal.RoundingMode.HALF_UP)
          yield Rate(from, to, price)
        case _ => Left(DecodingFailure("Invalid pair", c.history))
    }
  yield RateSet(rates.toSet)

case class Rate(from : String, to : String, value : BigDecimal):
  override def toString = s"$from -> $to: $value"
  def isIdentity: Boolean = from == to


object Rate:
  def identity(currency: String): Rate = Rate(currency, currency, 1.0)