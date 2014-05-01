package mpt.lab.two.automat.LexAutoTest

import org.scalatest.FlatSpec
import mpt.lab.two.lexem.{LexType, LexElem}
import scala.collection.mutable.ListBuffer
import mpt.lab.two.automat.{AutoPos, LexAuto}

/**
 * @author phpusr
 *         Date: 30.04.14
 *         Time: 15:34
 */

/**
 * Тестирование LexAuto (Stage D)
 */
class StageDSpec extends FlatSpec {

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
    assert(out(1).lexInfo.info.isEmpty)
    assert(out(1).constVal.get == 1)

    // Начало присваивания
    auto.makeLexList(Array("2:="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)
    assert(out(2).lexInfo.name == LexType.Const)
    assert(out(2).lexInfo.info.isEmpty)
    assert(out(2).constVal.get == 2)

    assert(out.size == 4)

    // Незначащие символы
    var eIndex = 4
    "(,).;".foreach { e =>
      auto.makeLexList(Array("3" + e.toString), out)
      assert(auto.currentState == AutoPos.F)

      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info.isEmpty)
      assert(out(eIndex).constVal.get == 3)

      assert(out(eIndex+1).lexInfo.name == LexType.MeaninglessSymbol)
      assert(out(eIndex+1).lexInfo.info.get == e.toString)
      assert(out(eIndex+1).constVal.isEmpty)

      eIndex += 2
    }

    // Символы-разделители
    " \t".foreach { e =>
      auto.makeLexList(Array("4" + e.toString), out)
      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info.isEmpty)
      assert(out(eIndex).constVal.get == 4)

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
