import io.circe.{Decoder, DecodingFailure, HCursor}
import cats.syntax.traverse.*
import cats.instances.list.*
import graph.{Edge, Node}

import scala.math.BigDecimal.RoundingMode

opaque type Currency = String

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
            price <- c.downField("rates").downField(pair).as[Double]
          yield Rate(from, to, price)
        case _ => Left(DecodingFailure("Invalid pair", c.history))
    }
  yield RateSet(rates.toSet)

case class Rate(from : String, to : String, value : Double):
  override def toString = f"$from -> $to: ${value%.2f}"
  def isIdentity: Boolean = from == to
  def toEdge: Edge = Edge(Node(from), Node(to), -Math.log(value.doubleValue))


object Rate:
  def fromEdge(edge: Edge): Rate = Rate(edge.from.name, edge.to.name, Math.exp(-edge.weight.doubleValue))
  def identity(currency: String): Rate = Rate(currency, currency, 1.0)