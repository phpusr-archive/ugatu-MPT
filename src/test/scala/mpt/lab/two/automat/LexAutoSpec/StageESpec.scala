package mpt.lab.two.automat.LexAutoSpec

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
 * Тестирование LexAuto (Stage E)
 */
class StageESpec extends FlatSpec {

  /** Проверка обработки знака присваивания */
  it should "processing :=" in {
    println("\n>> State G")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "abc2434(3@4#.:".foreach { e =>
      auto.makeLexList(Array("=" + e.toString), out) != LexAuto.NoErrors
    }

    val eIndex = out.size

    auto.makeLexList(Array("=="), out)
    assert(auto.currentState == AutoPos.F)
    assert(out(eIndex).lexInfo.name == LexType.Equals)
    assert(out(eIndex).lexInfo.info.get == "==")

    out.size == eIndex
  }

}
