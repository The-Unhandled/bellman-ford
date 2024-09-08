package swissborg.challenge.graph
import io.circe.jawn.decode
import org.scalatest.funsuite.AnyFunSuite
import swissborg.challenge.domain.RateUtils.*
import swissborg.challenge.domain.*

import scala.io.Source
import scala.math.BigDecimal.RoundingMode.HALF_UP

class BellmanFordSpec extends AnyFunSuite with RateUtils {

  test("BellmanFord should find shortest paths for given rates from file") {
    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    decode[RateSet](ratesJson) match {
      case Right(rateSet) =>
        val nodes = rateSet.rates.map(_.from).map(Node.apply)
        val edges = rateSet.rates.map(_.toEdge).toList

        val expectedRoute = List(
          Rate("BTC", "EUR", 0.1),
          Rate("EUR", "DAI", 0.09),
          Rate("DAI", "BTC", 1.86)
        )

        for
          startingNode <- nodes
          bellmanFord = BellmanFord(nodes, edges, startingNode)
          (distances, predecessors) = bellmanFord.findShortestPaths
          _ = println(s"Starting Node: $startingNode")
          _ = println(s"Distances from $startingNode: $distances")
          _ = println(s"Predecessors from $startingNode: $predecessors")
          rateList <- RateResolver.resolveRoutes(
            startingNode,
            distances,
            predecessors
          )
          profit = PrintTradeResolver.resolve(startingNode.name, rateList)
        yield
          rateList === expectedRoute

      case Left(error) =>
        fail(s"Failed to decode rates: $error")
    }
  }

  test("BellmanFord should find shortest paths for given matrix rates") {
    val testList = List(
      Rate("EUR", "DAI", 1.0211378960),
      Rate("DAI", "BTC", 0.0000429088),
      Rate("BTC", "EUR", 23258.8865583847)
    )

    PrintTradeResolver.resolve("EUR", testList, 100)

    val edgeSum = testList.map(_.toEdge).map(_.weight).sum
    println(s"edgeSum: $edgeSum")

    println("\n###\n")

    val nodes = Set("BTC", "BORG", "DAI", "EUR").map(Node.apply)

    val matrix = Array(
      Array(1.0, 116352.2654440156, 23333, 23258.8865583847), // BTC
      Array(0.0000086866, 1.0, 0.2053990550, 0.2017539914), // BORG
      Array(0.0000429088, 4.9320433378, 1.0, 0.9907652193), // DAI
      Array(0.0000435564, 5.0427577751, 1.0211378960, 1.0) // EUR
    )

    val edges = for {
      (fromNode, i) <- nodes.zipWithIndex
      (toNode, j) <- nodes.zipWithIndex
      rate = Rate(fromNode.name, toNode.name, matrix(i)(j))
    } yield rate.toEdge

    val expectedRoute = List(
      Rate("BTC", "EUR", 0.086),
      Rate("EUR", "DAI", 0.021),
      Rate("DAI", "BTC", 4.290880000000003e-5)
    )

    for
      startingNode <- nodes
      bellmanFord = BellmanFord(nodes, edges.toList, startingNode)
      (distances, predecessors) = bellmanFord.findShortestPaths
      _ = println(s"Starting Node: $startingNode")
      _ = println(s"Distances from $startingNode: $distances")
      _ = println(s"Predecessors from $startingNode: $predecessors")
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      profit = PrintTradeResolver.resolve(startingNode.name, rateList, 100)
    yield
      rateList === expectedRoute
      assert(BigDecimal(profit).setScale(2, HALF_UP) == 1.91)
  }
}
