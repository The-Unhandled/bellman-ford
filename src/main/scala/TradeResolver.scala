import scala.annotation.tailrec

/** @author
  *   Petros Siatos
  */

trait TradeResolver:
  /** Returns percentage value of trade (assuming cyclic trade) (e.g. 1.1 for
    * 10% profit)
    */
  def resolve(
      startingCurrency: String,
      trades: List[Rate],
      startingValue: BigDecimal
  ): BigDecimal

object PrintTradeResolver extends TradeResolver:
  def resolve(
      startingCurrency: String,
      trades: List[Rate],
      startingValue: BigDecimal = 1
  ): BigDecimal =

    @tailrec
    def calculate(
        index: Int,
        currentValue: BigDecimal,
        trades: List[Rate]
    ): BigDecimal = trades match
      case Nil => currentValue
      case trade :: nextTrades =>
        val nextValue = currentValue * trade.value
        println(
          s"$index Trade $currentValue ${trade.from} for $nextValue ${trade.to}"
        )
        calculate(index + 1, nextValue, nextTrades)

    println(s"Starting currency: $startingCurrency")
    val endingValue = calculate(1, startingValue, trades)
    val percentage = (endingValue - startingValue) / 100
    println(
      s"Profit: ${(percentage * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP)}%"
    )
    percentage
