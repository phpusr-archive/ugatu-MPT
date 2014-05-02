package mpt.lab.two.automat.LexAutoSpec

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
 * Тестирование LexAuto (Stage P)
 */
class StagePSpec extends FlatSpec {

  /** Проверка обработки числовых констант */
  it should "processing digit" in {
    println("\n>> State P")

    val out = ListBuffer[LexElem]()
    val auto = new LexAuto

    // Добавление цифр к числу
    auto.makeLexList(Array("01234.56789"), out)
    assert(auto.prevState == AutoPos.P)

    // Начало комментария
    auto.makeLexList(Array("1.1{"), out)
    assert(auto.currentState == AutoPos.C)
    assert(out(1).lexInfo.name == LexType.Const)
    assert(out(1).lexInfo.info.isEmpty)
    var value = out(1).constVal.get.asInstanceOf[Float]
    assert(Math.abs(value - 1.1) < 0.001)

    // Начало присваивания
    auto.makeLexList(Array("2.1:="), out)
    assert(auto.prevState == AutoPos.F)
    assert(auto.currentState == AutoPos.F)
    assert(out(2).lexInfo.name == LexType.Const)
    assert(out(2).lexInfo.info.isEmpty)
    value = out(2).constVal.get.asInstanceOf[Float]
    assert(Math.abs(value - 2.1) < 0.001)

    assert(out.size == 4)

    // Незначащие символы
    var eIndex = 4
    "();".foreach { e =>
      auto.makeLexList(Array("3.1" + e.toString), out)
      assert(auto.currentState == AutoPos.F)

      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info.isEmpty)
      value = out(eIndex).constVal.get.asInstanceOf[Float]
      assert(Math.abs(value - 3.1) < 0.001)

      assert(out(eIndex+1).lexInfo.name == LexType.Splitter)
      assert(out(eIndex+1).lexInfo.info.get == e.toString)
      assert(out(eIndex+1).constVal.isEmpty)

      eIndex += 2
    }

    // Символы-разделители
    " \t".foreach { e =>
      auto.makeLexList(Array("4.1" + e.toString), out)
      assert(out(eIndex).lexInfo.name == LexType.Const)
      assert(out(eIndex).lexInfo.info.isEmpty)
      value = out(eIndex).constVal.get.asInstanceOf[Float]
      assert(Math.abs(value - 4.1) < 0.001)

      eIndex += 1
    }

    // Не поддерживаемые символы
    ",!№%?*".foreach { e =>
      auto.makeLexList(Array("5.1" + e.toString), out) != LexAuto.NoErrors
    }
  }

}
