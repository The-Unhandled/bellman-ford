import graph.{Edge, Node}

object RateSetToEdgeTransformer:
  def transform(
      rateSet: RateSet
  ): (Map[String, Map[String, BigDecimal]], Set[Node], List[Edge]) =
    val rateMap =
      rateSet.rates.map(rate => rate.from -> Map(rate.to -> rate.value)).toMap
    val nodes = rateSet.rates.map(_.from).map(Node.apply)
    val edges =
      for
        from <- nodes
        (to, price) <- rateMap(from.name)
        weight = BigDecimal(-Math.log(price.toDouble))
        edge = Edge(from, Node(to), weight)
      yield edge

    (rateMap, nodes, edges.toList)
