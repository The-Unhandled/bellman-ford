package forsaken.bellmanford.domain

import scala.annotation.tailrec

trait TradeResolver:
  /** Returns percentage value of trade (assuming cyclic trade) (e.g. 1.1 for
    * 10% profit)
    */
  def resolve(
      startingCurrency: String,
      trades: List[Rate],
      startingValue: Double
  ): Double

/** Prints the trades to the console
  */
object PrintTradeResolver extends TradeResolver:
  def resolve(
      startingCurrency: String,
      trades: List[Rate],
      startingValue: Double = 1
  ): Double =

    @tailrec
    def calculate(
        index: Int,
        currentValue: Double,
        trades: List[Rate]
    ): Double = trades match
      case Nil => currentValue
      case trade :: nextTrades =>
        val nextValue = currentValue * trade.value
        println(
          f"$index Trade $currentValue ${trade.from} for $nextValue ${trade.to}"
          // f"$index Trade $currentValue%.2f ${trade.from} for $nextValue%.2f ${trade.to}"
        )
        calculate(index + 1, nextValue, nextTrades)

    println("")
    if trades.isEmpty then
      println("No arbitrage opportunities found")
      0
    else
      val endingValue = calculate(1, startingValue, trades)
      val percentage = (endingValue / startingValue) * 100
      val profitPercentage = percentage - 100
      println(f"Profit: $profitPercentage%.2f%%")
      profitPercentage
