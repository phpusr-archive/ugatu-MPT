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
    println("\n>> State H")

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

  //------------------ State C ------------------//

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

  //------------------ State G ------------------//

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
    assert(out(0).lexInfo.info == ":=")

    out.size == 1

  }

  //------------------ State V ------------------//

  /** Проверка обработки идентификаторов */
  it should "processing id" in {
    println("\n>> State V")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    // Добавление символов к имени ид-а
    auto.makeLexList(Array("vabcdefghijklmnopqrstuvwxyz_0123456789_ABCXYZ"), out)
    assert(auto.prevState == AutoPos.V)

    // Начало комментария
    auto.makeLexList(Array("v{"), out)
    assert(auto.currentState == AutoPos.C)
    assert(out(1).lexInfo.name == LexType.Var)
    assert(out(1).lexInfo.info == "v")

    // Начало присваивания
    auto.makeLexList(Array("d:="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)
    assert(out(2).lexInfo.name == LexType.Var)
    assert(out(2).lexInfo.info == "d")

    assert(out.size == 4)

    // Незначащие символы
    var eIndex = 4
    "(,).;".foreach { e =>
      auto.makeLexList(Array("w" + e.toString), out)
      assert(auto.currentState == AutoPos.F)
      assert(out(eIndex).lexInfo.name == LexType.Var)
      assert(out(eIndex).lexInfo.info == "w")
      assert(out(eIndex+1).lexInfo.name == LexType.MeaninglessSymbol)
      assert(out(eIndex+1).lexInfo.info == e.toString)

      eIndex += 2
    }

    // Символы-разделители
    " \t".foreach { e =>
      auto.makeLexList(Array("r" + e.toString), out)
      assert(out(eIndex).lexInfo.name == LexType.Var)
      assert(out(eIndex).lexInfo.info == "r")

      eIndex += 1
    }

    // Не поддерживаемые символы
    val outSize = out.size
    "!№%?*".foreach { e =>
      intercept[MatchError] {
        auto.makeLexList(Array("s" + e.toString), out)
      }
    }

    assert(out.size == outSize)
  }


  //------------------ State D ------------------//

  /** Проверка обработки числовых констант */
  it should "processing digit" in {
    println("\n>> State D")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    // Добавление цифр к числу
    auto.makeLexList(Array("0123456789"), out)
    assert(auto.prevState == AutoPos.D)

    // Начало комментария
    auto.makeLexList(Array("1{"), out)
    assert(auto.currentState == AutoPos.C)
    assert(out(1).lexInfo.name == LexType.Const)
    assert(out(1).lexInfo.info == "1")

    // Начало присваивания
    auto.makeLexList(Array("2:="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)
    assert(out(2).lexInfo.name == LexType.Const)
    assert(out(2).lexInfo.info == "2")

    assert(out.size == 4)

    // Незначащие символы
    var eIndex = 4
    "(,).;".foreach { e =>
      auto.makeLexList(Array("3" + e.toString), out)
      assert(auto.currentState == AutoPos.F)
      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info == "3")
      assert(out(eIndex+1).lexInfo.name == LexType.MeaninglessSymbol)
      assert(out(eIndex+1).lexInfo.info == e.toString)

      eIndex += 2
    }

    // Символы-разделители
    " \t".foreach { e =>
      auto.makeLexList(Array("4" + e.toString), out)
      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info == "4")

      eIndex += 1
    }

    // Не поддерживаемые символы
    val outSize = out.size
    "!№%?*".foreach { e =>
      intercept[MatchError] {
        auto.makeLexList(Array("5" + e.toString), out)
      }
    }

    assert(out.size == outSize)
  }

}
