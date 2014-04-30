package mpt.lab.two.automat

import org.scalatest.FlatSpec
import mpt.lab.two.lexem.{LexType, LexElem}
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 30.04.14
 *         Time: 15:34
 */

/**
 * Тестирование LexAuto
 */
class LexAutoSpec extends FlatSpec {

  //------------------ State H ------------------//

  /** Проверка добавления символов */
  it should "add whitespaces and splitters" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    auto.makeLexList(Array("("), out)
    assert(auto.currentState == AutoPos.F)
    assert(out(0).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(0).lexInfo.info == "(")

    auto.makeLexList(Array(","), out)
    assert(out(1).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(1).lexInfo.info == ",")

    auto.makeLexList(Array(")"), out)
    assert(out(2).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(2).lexInfo.info == ")")

    auto.makeLexList(Array(";"), out)
    assert(out(3).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(3).lexInfo.info == ";")

    auto.makeLexList(Array(" "), out)
    assert(auto.currentState == AutoPos.F)
    assert(out(4).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(4).lexInfo.info == " ")

    out.size == 4
  }

  /** Проверка обработки букв */
  it should "processing letters" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "bcdfghjklmnpqrsuvwyz_ABCXYZ".foreach { e =>
      auto.makeLexList(Array(e.toString), out)
      assert(auto.currentState == AutoPos.V)
    }

    "iteoxa".foreach { e =>
      intercept[MatchError] {
        auto.makeLexList(Array(e.toString), out)
      }
    }

    out.size == 0
  }


  /** Проверка изменения состояния */
  it should "change state and nothing to add" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    auto.makeLexList(Array("{"), out)
    assert(auto.currentState == AutoPos.C)

    auto.makeLexList(Array(":"), out)
    assert(auto.currentState == AutoPos.G)

    for (i <- 0 to 9) {
      auto.makeLexList(Array(i.toString), out)
      assert(auto.currentState == AutoPos.D)
    }

    out.size == 0
  }

  /** Проверка обработки не поддерживаемых символов */
  it should "throw MatchError" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "`~!@#$%^&*-=+\\/".foreach { e =>
      intercept[MatchError] {
        auto.makeLexList(Array(e.toString), out)
      }
    }

    out.size == 0
  }


  //------------------ State C ------------------//

  /** Проверка обработки комментариев */
  it should "processing comments" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "bcdfghjklmnpqrsuvwyz_ABCXYZ".foreach { e =>
      auto.makeLexList(Array("{", e.toString), out)
      assert(auto.currentState == AutoPos.C)
    }

    auto.makeLexList(Array("{comment}"), out)
    assert(auto.currentState == AutoPos.F)
  }


}
