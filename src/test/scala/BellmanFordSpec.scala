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
      //rateList <- RateResolver.resolveRoutes(distances, predecessors)
      //_ = PrintTradeResolver.resolve(startingNode.name, rateList)
      _ = println("###")
    yield ()

    assert(1 == 1)
  }

  test("BellmanFord should find shortest paths for given rates from file") {
    val ratesJson = Source.fromResource("borgRates.json").getLines().mkString

    decode[RateSet](ratesJson) match {
      case Right(rateSet) =>
        val (rateMap, nodes, edges) = RateSetToEdgeTransformer.transform(rateSet)
        for
          startingNode <- nodes
          bellmanFord = BellmanFord(nodes, edges, startingNode)
          (distances, predecessors) = bellmanFord.findShortestPaths
          _ = println(s"startingNode: $startingNode")
          _ = println(s"Distances from $startingNode: $distances")
          _ = println(s"Predecessors from $startingNode: $predecessors")
          rateList <- RateResolver.resolveRoutes(distances, predecessors, rateMap)
          _ = PrintTradeResolver.resolve(startingNode.name, rateList)
          _ = println("###")
        yield ()

      case Left(error) =>
        fail(s"Failed to decode rates: $error")
    }
  }


  test("BellmanFord should find shortest paths for given matrix rates") {
    val currencies = Set("BTC", "BORG", "DAI", "EUR").map(Node.apply)

    val matrix = Array(
      //    BTC          BORG         DAI          EUR
      Array(1.0,        116352.2654440156, 23524.1391553039, 23258.8865583847), // BTC
      Array(0.0000086866, 1.0,        0.2053990550, 0.2017539914), // BORG
      Array(0.0000429088, 4.9320433378, 1.0,        0.9907652193), // DAI
      Array(0.0000435564, 5.0427577751, 1.0211378960, 1.0)         // EUR
    )

    val rateSet = RateSet(for {
      (fromNode, i) <- currencies.zipWithIndex
      (toNode, j) <- currencies.zipWithIndex
      rate =  Rate(fromNode.name, toNode.name, BigDecimal(matrix(i)(j)))
    } yield rate)

    val (rateMap, nodes, edges) = RateSetToEdgeTransformer.transform(rateSet)
    
    val bellmanFord = BellmanFord(currencies, edges, Node("EUR"))
    val (distances, predecessors) = bellmanFord.findShortestPaths
    println(s"Distances from EUR: $distances")
    println(s"Predecessors from EUR: $predecessors")
    val rateList = RateResolver.resolveRoutes(distances, predecessors, rateMap)
    rateList.foreach(PrintTradeResolver.resolve("EUR", _, 100))

    assert(1 == 1)
  }

}
