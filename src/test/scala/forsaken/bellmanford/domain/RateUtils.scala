package forsaken.bellmanford.domain

import scala.annotation.targetName

trait RateUtils:
  def assertRateEquals(
      expected: Rate,
      actual: Rate,
      tolerance: Double = 0.01
  ): Unit =
    assert(expected.from == actual.from)
    assert(expected.to == actual.to)
    assert((expected.value - actual.value).abs <= tolerance)

object RateUtils extends RateUtils:
  implicit class RateListOps(val rates: List[Rate]) extends AnyVal:
    @targetName("assertRateListEquals")
    def ===(other: List[Rate])(implicit tolerance: Double = 0.01): Unit =
      assert(
        rates.size == other.size,
        s"Expected size ${other.size}, but got ${rates.size}"
      )
      rates.zip(other).foreach { case (expected, actual) =>
        assertRateEquals(expected, actual, tolerance)
      }
