package mpt.lab.two.automat.LexAutoTest

import mpt.lab.two.lexem.LexElem
import mpt.lab.two.automat.{AutoPos, LexAuto}
import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 12:54
 */

/**
 * Тестирование LexAuto (Stage H)
 */
class StageCSpec extends FlatSpec {

  /** Проверка обработки комментариев */
  it should "processing comments" in {
    println("\n>> State C")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "bcdfghjklmnpqrsuvwyz_ABCXYZ".foreach { e =>
      auto.makeLexList(Array("{" + e.toString), out)
      assert(auto.currentState == AutoPos.C)
    }

    auto.makeLexList(Array("{comment}"), out)
    assert(auto.currentState == AutoPos.F)

    out.size == 0
  }

}
