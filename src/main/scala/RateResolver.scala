import graph.{Edge, Node}

import scala.annotation.tailrec

object RateResolver:

  def resolveRoutes(
      startingNode: Node,
      distances: Map[Node, Double],
      predecessors: Map[Node, Option[Edge]]
  ): List[List[Rate]] =

    def keepCycle(route: List[Rate]): List[Rate] =
      route.reverse.dropWhile(_.to != route.head.from).reverse

    @tailrec
    def resolveNodeRoutes(current: Node, rateList: List[Rate]): List[Rate] =
      val visitedNodes = rateList.map(_.to).toSet
      predecessors(current) match {
        case None =>
          rateList
        // Negative Cycle found
        case Some(edge @ Edge(from, _, _))  if visitedNodes.contains(from.name) =>
          Rate.fromEdge(edge) :: rateList
          keepCycle(Rate.fromEdge(edge) :: rateList)
        case Some(edge) =>
          resolveNodeRoutes(edge.from, Rate.fromEdge(edge) :: rateList)
      }

    (for
      // Only find negative cycles
      node <- predecessors.keys if distances(node) < 0
      list = resolveNodeRoutes(startingNode, List.empty)
    yield list).toList
