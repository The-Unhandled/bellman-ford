import scala.annotation.tailrec
import scala.collection.immutable.Queue

/**
 * @author Petros Siatos
 */

trait TradeResolver:
    def resolve(trades: List[Rate], startingValue: BigDecimal): BigDecimal

object PrintTradeResolver extends TradeResolver:
    def resolve(trades: List[Rate], startingValue: BigDecimal = 100): BigDecimal =

      @tailrec
      def calculate(index: Int, currentValue: BigDecimal, trades: List[Rate]): BigDecimal = trades match
        case Nil => currentValue
        case trade :: nextTrades =>
          val nextValue = currentValue * trade.value
          println(s"$index Trade $currentValue ${trade.from} for $nextValue ${trade.to}")
          calculate(index + 1, nextValue, nextTrades)

      calculate(1, startingValue, trades)