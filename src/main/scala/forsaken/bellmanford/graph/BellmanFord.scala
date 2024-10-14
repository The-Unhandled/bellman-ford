package forsaken.bellmanford.graph

import scala.collection.mutable
import scala.collection.mutable.Map as MutableMap

/** Implementation of the Bellman Ford algorithm to find the shortest path in a
  * graph.
  *
  * @see
  *   [[https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm Bellman Ford Algorithm]]
  * @see
  *   [[https://dev.to/optiklab/algorithmic-alchemy-exploiting-graph-theory-in-the-foreign-exchange-399k Graph Theory in the Foreign Exchange]]
  * @see
  *   [[https://github.com/williamfiset/Algorithms/blob/master/src/main/java/com/williamfiset/algorithms/graphtheory/BellmanFordEdgeList.java William Fiset - Bellmman Ford EdgeList]]
  * @see
  *   [[https://anilpai.medium.com/currency-arbitrage-using-bellman-ford-algorithm-8938dcea56ea Currency Arbitrage using Bellman Ford Algorithm]]
 * @see
 *    [[https://www.ijisrt.com/assets/upload/files/IJISRT20MAY047.pdf Currency Arbitrage Detection]]
  */
class BellmanFord(nodes: Set[Node], edges: List[Edge], startingNode: Node):

  def findShortestPaths: (Map[Node, Double], Map[Node, Option[Edge]]) =

    // Step 1: initialize graph
    val distance: MutableMap[Node, Double] =
      mutable.HashMap(nodes.map(_ -> Double.PositiveInfinity).toSeq: _*)

    val predecessor: MutableMap[Node, Option[Edge]] =
      mutable.HashMap(nodes.map(_ -> None).toSeq: _*)

    distance(startingNode) = 0

    // Step 2: relax edges repeatedly
    var relaxedAnEdge = true
    for (_ <- 1 until nodes.size if relaxedAnEdge) {
      relaxedAnEdge = false
      for (edge <- edges) {
        val newDistance = distance(edge.from) + edge.weight
        if (newDistance < distance(edge.to)) {
          distance(edge.to) = newDistance
          predecessor(edge.to) = Some(edge)
          relaxedAnEdge = true
        }
      }
    }

    // Step 3: check for negative-weight cycles
    for (_ <- 1 until nodes.size if relaxedAnEdge) {
      relaxedAnEdge = false
      for (edge <- edges) {
        if (distance(edge.from) + edge.weight < distance(edge.to)) {
          distance(edge.to) = Double.NegativeInfinity
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
