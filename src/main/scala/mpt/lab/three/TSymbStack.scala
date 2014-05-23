package mpt.lab.three

import scala.collection.mutable.ListBuffer
import mpt.lab.three.Types.TLexem

/**
 * @author phpusr
 *         Date: 23.05.14
 *         Time: 16:43
 */

/**
 * Синтаксический стек
 */
class TSymbStack {

  private val items = ListBuffer[TSymbol]() //TODO возмоно это не список символов

  private val symbols: ListBuffer[TSymbol] = ??? //TODO


  /** Очистка стека */
  def clear() = items.clear()

  /** Выборка символа по номеру от вершины стека */
  def getSymbol(index: Int): TSymbol = items(index)

  /** Помещения в стек входящей лексемы */
  def push(lex: TLexem): TSymbol = {
    val lexSymb = TSymbol.createLex(lex)
    items += lexSymb

    lexSymb
  }

  /** Самая верхняя лексема в стеке */
  def topLexem = {
    val find = symbols.reverse.find(_.symbType == TSymbKind.SymbLex)
    if (find.isDefined) {
      Some(find.get.lexem)
    } else None
  }

  /** Удаление элемента из стека */
  def delete(index: Int) = items.remove(index)

  /** Свертка и помещение нового символа на вершину стека */
  def makeTopSymb = {
    val symbArr: Array[TSymbol] = ???
    var iSymbN = 0
    var i = 0

    /** Строка правила */
    var sRuleStr = ""

    /** Текущий символ */
    var symCur: TSymbol = null

    val addToRule = (sStr: String, sym: TSymbol) => {
      symCur = sym
      symbArr(iSymbN) = symbols(i)
      sRuleStr += sStr
      delete(i)
      iSymbN += 1
    }

    i = symbols.size
    symbols.reverse.foreach { s =>
      i -= 1

      if (s.symbType == TSymbKind.SymbSynt) {
        addToRule(s.symbolStr, symCur)
      } else {
        if (symCur == null) {
          addToRule(s.lexem.lexInfo.name, s)
        } else {
          val rowIndex = s.lexem.lexInfo.name.toInt //TODO
          val columnIndex = symCur.lexem.lexInfo.name.toInt //TODO
          if (SyntRule.GrammMatrix(rowIndex)(columnIndex) == SyntRule.Equals) {
            addToRule(s.lexem.lexInfo.name, s)
          } else {
            //TODO прерываем цикл
          }
        }
      }

      if (iSymbN > SyntSymb.RuleLength) {
        //TODO прервать цикл
      }
    }

    var symbol: Option[TSymbol] = None

    // Если выбран хотя бы один символ из стека
    if (iSymbN != 0) {
      val find = SyntRule.GrammRules.find(_ == sRuleStr)
      if (find.isDefined) {
        symbol = Some(TSymbol.createSymb(i, iSymbN, symbArr))
        items += symbol.get
      } else {
        for (i <- 0 until iSymbN) symbArr(i) = null
      }
    }

    symbol
  }

  /** Кол-во элементов в стеке */
  def count = items.size

}
