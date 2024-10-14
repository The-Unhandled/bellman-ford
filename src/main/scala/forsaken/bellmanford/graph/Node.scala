package forsaken.bellmanford.graph

case class Node(name: String):
  override def toString: String = name

case class Edge(from: Node, to: Node, weight: Double):
  override def toString: String = s"$from -> $to: $weight"

object Edge:
  def apply(from: String, to: String, weight: Double): Edge =
    Edge(Node(from), Node(to), weight)
