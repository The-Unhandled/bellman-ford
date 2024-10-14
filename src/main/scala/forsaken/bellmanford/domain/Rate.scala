package forsaken.bellmanford.domain

import forsaken.bellmanford.graph.{Edge, Node}

case class Rate(from: String, to: String, value: Double):
  override def toString = f"$from -> $to: ${value % .2f}"
  def isIdentity: Boolean = from == to
  def toEdge: Edge = Edge(Node(from), Node(to), -Math.log(value.doubleValue))

object Rate:
  def fromEdge(edge: Edge): Rate =
    Rate(edge.from.name, edge.to.name, Math.exp(-edge.weight.doubleValue))
  def identity(currency: String): Rate = Rate(currency, currency, 1.0)
