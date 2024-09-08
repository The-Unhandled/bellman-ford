package swissborg.challenge

import swissborg.challenge.client.SwissborgRatesClient
import swissborg.challenge.domain.{PrintTradeResolver, RateResolver}
import swissborg.challenge.graph.{BellmanFord, Node}

object ChallengeApp:
  def run(url: String): Unit =
    println("Starting Challenge App")
    println(s"Url chosen: $url")

    val client = SwissborgRatesClient(url)

    val rateSet = client.fetchRates
    println(s"Fetched ${rateSet.rates.size} rates")

    val nodes = rateSet.rates.map(_.from).map(Node.apply)
    val edges = rateSet.rates.map(_.toEdge).toList

    val startingNode = nodes.head
    println(s"Starting Node: $startingNode")
    val bellmanFord = BellmanFord(nodes, edges, startingNode)
    val (distances, predecessors) = bellmanFord.findShortestPaths
    // println(s"Distances from $startingNode: $distances")
    // println(s"Predecessors from $startingNode: $predecessors")
    for
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      _ = PrintTradeResolver.resolve(startingNode.name, rateList, 100)
    yield ()
