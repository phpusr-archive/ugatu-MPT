package mpt.lab.two.automat.LexAutoTest

import mpt.lab.two.lexem.{LexType, LexElem}
import mpt.lab.two.automat.{AutoPos, LexAuto}
import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 12:40
 */

/**
 * Тестирование LexAuto (Stage H)
 */
class StageHSpec extends FlatSpec {

  /** Проверка добавления символов */
  it should "add whitespaces and splitters" in {
    println("\n>> State H")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    auto.makeLexList(Array("("), out)
    assert(auto.currentState == AutoPos.F)
    assert(out(0).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(0).lexInfo.info.get == "(")

    auto.makeLexList(Array(","), out)
    assert(out(1).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(1).lexInfo.info.get == ",")

    auto.makeLexList(Array(")"), out)
    assert(out(2).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(2).lexInfo.info.get == ")")

    auto.makeLexList(Array(";"), out)
    assert(out(3).lexInfo.name == LexType.MeaninglessSymbol)
    assert(out(3).lexInfo.info.get == ";")

    auto.makeLexList(Array(" "), out)
    assert(auto.currentState == AutoPos.F)

    out.size == 3
  }

  /** Проверка обработки букв */
  it should "processing letters" in {
    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    "bcdfghjklmnpqrsuvwyz_ABCXYZ".foreach { e =>
      auto.makeLexList(Array(e.toString), out)
      assert(auto.prevState == AutoPos.V)
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

    auto.makeLexList(Array(":="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)

    for (i <- 0 to 9) {
      auto.makeLexList(Array(i.toString), out)
      assert(auto.prevState == AutoPos.D)
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

}
