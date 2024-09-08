package swissborg.challenge.domain

import io.circe.{Decoder, DecodingFailure, HCursor}
import swissborg.challenge.Rate
import swissborg.challenge.graph.{Edge, Node}

import scala.math.BigDecimal.RoundingMode

case class Rate(from: String, to: String, value: Double):
  override def toString = f"$from -> $to: ${value % .2f}"
  def isIdentity: Boolean = from == to
  def toEdge: Edge = Edge(Node(from), Node(to), -Math.log(value.doubleValue))

object Rate:
  def fromEdge(edge: Edge): Rate =
    Rate(edge.from.name, edge.to.name, Math.exp(-edge.weight.doubleValue))
  def identity(currency: String): Rate = Rate(currency, currency, 1.0)
