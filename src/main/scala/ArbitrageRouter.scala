import scala.annotation.tailrec
import scala.collection.immutable.List}

/**
 * @author Petros Siatos
 */

trait ArbitrageRouter
  def findBest(rateSet: RateSet): List[Rate]


object BruteForceArbitrageRouter

  def findBest(rateSet: RateSet): List[Rate] =
    val rateMap = rateSet.rates
      .filterNot(_.isIdentity)
      .toList
      .groupBy(_.from)
      .view.mapValues(_.map(rate => rate.to -> rate.value).toMap)
      .toMap

    val currencies = rateMap.keys.toSet
    println(s"Found ${currencies.size} currencies.")

    @tailrec
    def findRoutes(baseCurrency: String,
                   bestRoute: List[Rate],
                   tradeRoute: List[Rate],
                   ): List[Rate] =
      val currenciesLeft = currencies -- tradeRoute.map(_.to)
      currenciesLeft.headOption match
        case Some(nextCurrency) =>
          val newRoute = rateMap.get(tradeRoute.last.to).flatMap(_.find(_.to == nextCurrency))
          findRoutes(baseCurrency, bestRoute, tradeRoute ++ newRoute)
        case None => ???

    for
      currency <- currencies
      bestRoute <- findRoutes(currency,List(Rate.identity(currency)),  List.empty)
    yield bestRoute

