package mpt.lab.two.automat.LexAutoTest

import org.scalatest.FlatSpec
import mpt.lab.two.lexem.{LexType, LexElem}
import mpt.lab.two.automat.{AutoPos, LexAuto}
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 12:55
 */

/**
 * Тестирование LexAuto (Stage G)
 */
class StageGSpec extends FlatSpec {

  /** Проверка обработки знака присваивания */
  it should "processing :=" in {
    println("\n>> State G")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "abc2434(3@4#.:".foreach { e =>
      intercept[MatchError] {
        auto.makeLexList(Array(":" + e.toString), out)
      }
    }

    out.size == 0

    auto.makeLexList(Array(":="), out)
    assert(auto.currentState == AutoPos.F)
    assert(out(0).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(0).lexInfo.info.get == ":=")

    out.size == 1

  }

}
