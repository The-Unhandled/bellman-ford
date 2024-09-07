package graph

import scala.math.BigDecimal.RoundingMode

case class Node(name: String):
  override def toString: String = name

case class Edge(from: Node, to: Node, weight: BigDecimal):
  override def toString: String = s"$from -> $to: ${weight.setScale(2, RoundingMode.HALF_UP)}"

object Edge:
  def apply(from: String, to: String, weight: BigDecimal): Edge =
    Edge(Node(from), Node(to), weight)
