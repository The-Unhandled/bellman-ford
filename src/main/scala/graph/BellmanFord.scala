package graph

import scala.collection.mutable
import scala.collection.mutable.Map as MutableMap

/** @see
  *   [[https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm Bellman Ford Algorithm]]
  * @see
  *   [[https://dev.to/optiklab/algorithmic-alchemy-exploiting-graph-theory-in-the-foreign-exchange-399k Graph Theory in the Foreign Exchange]]
  * @see
  *   [[https://github.com/williamfiset/Algorithms/blob/master/src/main/java/com/williamfiset/algorithms/graphtheory/BellmanFordEdgeList.java William Fiset - Bellmman Ford EdgeList]]
  */
class BellmanFord(nodes: Set[Node], edges: List[Edge], startingNode: Node):

  def findShortestPaths: (Map[Node, BigDecimal], Map[Node, Option[Edge]]) =

    // Step 1: initialize graph
    val distance: MutableMap[Node, BigDecimal] =
      mutable.HashMap(nodes.map(_ -> BigDecimal(Double.MaxValue)).toSeq: _*)

    val predecessor: MutableMap[Node, Option[Edge]] =
      mutable.HashMap(nodes.map(_ -> None).toSeq: _*)

    distance(startingNode) = 0

    // Step 2: relax edges repeatedly
    var relaxedAnEdge = true
    for (_ <- 1 until nodes.size if relaxedAnEdge) {
      relaxedAnEdge = false
      for (edge <- edges) {
        if (distance(edge.from) + edge.weight < distance(edge.to)) {
          distance(edge.to) = distance(edge.from) + edge.weight
          predecessor(edge.to) = Some(edge)
          relaxedAnEdge = true
        }
      }
    }

    // Step 3: check for negative-weight cycles
    relaxedAnEdge = true
    for (_ <- 1 until nodes.size if relaxedAnEdge) {
      relaxedAnEdge = false
      for (edge <- edges) {
        if (distance(edge.from) + edge.weight < distance(edge.to)) {
          distance(edge.to) = BigDecimal(Double.MinValue)
          relaxedAnEdge = true
        }
      }
    }

    (distance.toMap, predecessor.toMap)

object BellmanFord:
  def apply(
      nodes: Set[Node],
      edges: List[Edge],
      startingNode: Node
  ): BellmanFord =
    new BellmanFord(nodes, edges, startingNode)
