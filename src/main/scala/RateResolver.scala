import graph.{Edge, Node}

import scala.annotation.tailrec

object RateResolver:

  def resolveRoutes(
      distances: Map[Node, BigDecimal],
      predecessors: Map[Node, Option[Edge]],
      rateMap: Map[String, Map[String, BigDecimal]],
  ): List[List[Rate]] =

    @tailrec
    def resolveNodeRoutes(current: Node, rateList: List[Rate]): List[Rate] =
      predecessors(current) match {
        case None =>
          rateList
        case Some(edge) =>
          val rate = Rate(edge.from.name, edge.to.name, rateMap(edge.from.name)(edge.to.name)) 
          resolveNodeRoutes(edge.from, rate :: rateList)
      }

    (for
      // Do not include negative cycles
      node <- predecessors.keys if distances(node) > 0
      list = resolveNodeRoutes(node, List.empty)
    yield list).toList
