package swissborg.challenge.graph
import org.scalatest.funsuite.AnyFunSuite
import swissborg.challenge.domain.{PrintTradeResolver, RateResolver}

import scala.util.Random

class BellmanFordStressSpec extends AnyFunSuite {

  ignore("Bellman-Ford stress test with 400 currencies") {
    val numCurrencies = 400
    val random = new Random()

    // Generate 1000 unique currency nodes
    val nodes = (1 to numCurrencies).map(i => Node(s"Currency$i")).toSet

    // Generate random edges between nodes with random weights
    val edges = for {
      fromNode <- nodes
      toNode <- nodes if fromNode != toNode
      weight = random.nextDouble() * 10 - 5
    } yield Edge(fromNode, toNode, weight)

    // Pick a random starting node
    val startingNode = nodes.head

    // Run Bellman-Ford algorithm
    val before = System.nanoTime
    val bellmanFord = BellmanFord(nodes, edges.toList, startingNode)
    val (distances, predecessors) = bellmanFord.findShortestPaths
    val after = System.nanoTime
    println("Bellman Ford Elapsed time: " + (after - before) / 1000000 + "ms")

    val before2 = System.nanoTime
    for
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      _ = PrintTradeResolver.resolve(startingNode.name, rateList, 100)
    yield ()
    val after2 = System.nanoTime
    println("Print Elapsed time: " + (after2 - before2) / 1000000 + "ms")

    // Assertions to ensure the algorithm runs without errors
    assert(distances.nonEmpty)
    assert(predecessors.nonEmpty)

    // 200 - Bellman Ford Elapsed time: ~1456ms first impl
    // 200 - BigDecimal -> Double ~400ms

    // 400 - 3300 - 3525ms
  }
}
