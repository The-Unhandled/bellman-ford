import org.scalatest.funsuite.AnyFunSuite
import graph.{BellmanFord, Edge, Node}
import io.circe.jawn.decode

import scala.io.Source

class BellmanFordSpec extends AnyFunSuite {

  test("BellmanFord should find shortest paths for given graph") {
    val nodes =
      Set("USD", "CHF", "YEN", "GBP", "CNY", "EUR", "XXX", "YYY").map(
        Node.apply
      )

    val INF = Double.PositiveInfinity
    val matrix = Array(
      //    USD  CHF  YEN  GBP   CNY  EUR  XXX  YYY
      Array(0.0, 1.0, INF, INF, INF, INF, INF, INF), // USD
      Array(INF, 0.0, 1.0, INF, INF, 4.0, 4.0, INF), // CHF
      Array(INF, INF, 0.0, INF, 1.0, INF, INF, INF), // YEN
      Array(INF, INF, 1.0, 0.0, INF, INF, INF, INF), // GBP
      Array(INF, INF, INF, -3.0, 0.0, INF, INF, INF), // CNY
      Array(INF, INF, INF, INF, INF, 0.0, 5.0, 3.0), // EUR
      Array(INF, INF, INF, INF, INF, INF, 0.0, 4.0), // XXX
      Array(INF, INF, INF, INF, INF, INF, INF, 0.0) // YYY
    )

    val edges = for {
      (fromNode, i) <- nodes.zipWithIndex
      (toNode, j) <- nodes.zipWithIndex
      if matrix(i)(j) != INF
    } yield Edge(fromNode, toNode, BigDecimal(matrix(i)(j)))

    for
      startingNode <- nodes
      bellmanFord = BellmanFord(nodes, edges.toList, startingNode)
      (distances, predecessors) = bellmanFord.findShortestPaths
      _ = println(s"startingNode: $startingNode")
      _ = println(s"Distances from $startingNode: $distances")
      _ = println(s"Predecessors from $startingNode: $predecessors")
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      _ = PrintTradeResolver.resolve(startingNode.name, rateList)
      _ = println("###")
    yield ()

    assert(1 == 1)
  }

  test("BellmanFord should find shortest paths for given rates from file") {
    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    decode[RateSet](ratesJson) match {
      case Right(rateSet) =>
        val nodes = rateSet.rates.map(_.from).map(Node.apply)
        val edges = rateSet.rates.map(_.toEdge).toList

        for
          startingNode <- nodes
          bellmanFord = BellmanFord(nodes, edges, startingNode)
          (distances, predecessors) = bellmanFord.findShortestPaths
          _ = println(s"startingNode: $startingNode")
          _ = println(s"Distances from $startingNode: $distances")
          _ = println(s"Predecessors from $startingNode: $predecessors")
          rateList <- RateResolver.resolveRoutes(
            startingNode,
            distances,
            predecessors
          )
          _ = PrintTradeResolver.resolve(startingNode.name, rateList)
          _ = println("###")
        yield ()

      case Left(error) =>
        fail(s"Failed to decode rates: $error")
    }
  }

  test("BellmanFord should find shortest paths for given matrix rates") {

    // Test sto tetragwno
    val testList = List(
      Rate("EUR", "DAI", 1.0211378960),
      Rate("DAI", "BTC", 0.0000429088),
      Rate("BTC", "EUR", 23258.8865583847)
    )

    PrintTradeResolver.resolve("EUR", testList, 100)

    val edgeSum = testList.map(_.toEdge).map(_.weight).sum
    println(s"edgeSum: $edgeSum")

    println("\n###\n")
    // End supertest

    val nodes = Set("BTC", "BORG", "DAI", "EUR").map(Node.apply)

    val matrix = Array(
      //    BTC          BORG         DAI          EUR
      Array(1.0, 116352.2654440156, 23333, 23258.8865583847), // BTC
      Array(0.0000086866, 1.0, 0.2053990550, 0.2017539914), // BORG
      Array(0.0000429088, 4.9320433378, 1.0, 0.9907652193), // DAI
      Array(0.0000435564, 5.0427577751, 1.0211378960, 1.0) // EUR
    )

    val edges = for {
      (fromNode, i) <- nodes.zipWithIndex
      (toNode, j) <- nodes.zipWithIndex
      rate = Rate(fromNode.name, toNode.name, BigDecimal(matrix(i)(j)))
    } yield rate.toEdge

    for
      startingNode <- nodes
      bellmanFord = BellmanFord(nodes, edges.toList, startingNode)
      (distances, predecessors) = bellmanFord.findShortestPaths
      _ = println(s"Distances from EUR: $distances")
      _ = println(s"Predecessors from EUR: $predecessors")
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      _ = PrintTradeResolver.resolve(startingNode.name, rateList, 100)
    yield ()

  }

}
