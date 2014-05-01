package mpt.lab.two.automat.LexAutoSpec

import mpt.lab.two.lexem.{LexType, LexElem}
import mpt.lab.two.automat.{LexAuto, AutoPos}
import org.scalatest.FlatSpec
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 12:12
 */

/**
 * Тестирование LexAuto (Stage V)
 */
class StateVSpec extends FlatSpec {

  /** Проверка обработки идентификаторов */
  it should "processing word" in {
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
    assert(out(1).lexInfo.info.isEmpty)
    assert(out(1).varInfo.get.name == "v")

    // Начало присваивания
    auto.makeLexList(Array("d:="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)
    assert(out(2).lexInfo.name == LexType.Var)
    assert(out(2).lexInfo.info.isEmpty)
    assert(out(2).varInfo.get.name == "d")

    assert(out.size == 4)

    // Незначащие символы
    var eIndex = 4
    "(,).;".foreach { e =>
      auto.makeLexList(Array("w" + e.toString), out)
      assert(auto.currentState == AutoPos.F)

      assert(out(eIndex).lexInfo.name == LexType.Var)
      assert(out(eIndex).lexInfo.info.isEmpty)
      assert(out(eIndex).varInfo.get.name == "w")

      assert(out(eIndex+1).lexInfo.name == LexType.SplitterSign)
      assert(out(eIndex+1).lexInfo.info.get == e.toString)
      assert(out(eIndex+1).varInfo.isEmpty)

      eIndex += 2
    }

    // Символы-разделители
    " \t".foreach { e =>
      auto.makeLexList(Array("r" + e.toString), out)
      assert(out(eIndex).lexInfo.name == LexType.Var)
      assert(out(eIndex).lexInfo.info.isEmpty)
      assert(out(eIndex).varInfo.get.name == "r")

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

}
