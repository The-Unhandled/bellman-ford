package forsaken.bellmanford

import forsaken.bellmanford.client.SwissborgRatesClient
import forsaken.bellmanford.domain.{PrintTradeResolver, RateResolver}
import forsaken.bellmanford.graph.{BellmanFord, Node}

object App:
  def run(url: String): Unit =
    println("Starting Challenge App")
    println(s"Url chosen: $url")

    // Fetch Rates.
    val client = SwissborgRatesClient(url)

    val rateSet = client.fetchRates
    println(s"Fetched ${rateSet.rates.size} rates")

    // Initialize Nodes and Edges.
    val nodes = rateSet.rates.map(_.from).map(Node.apply)
    val edges = rateSet.rates.map(_.toEdge).toList

    val startingNode = nodes.head
    println(s"Starting Node: $startingNode")

    // Run Bellman-Ford Algorithm
    val bellmanFord = BellmanFord(nodes, edges, startingNode)
    val (distances, predecessors) = bellmanFord.findShortestPaths
    // println(s"Distances from $startingNode: $distances")
    // println(s"Predecessors from $startingNode: $predecessors")

    // Resolve routes and print trades.
    for
      rateList <- RateResolver.resolveRoutes(
        startingNode,
        distances,
        predecessors
      )
      _ = PrintTradeResolver.resolve(startingNode.name, rateList, 100)
    yield ()
