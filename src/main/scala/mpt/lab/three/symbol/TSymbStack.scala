package mpt.lab.three.symbol

import scala.collection.mutable.ListBuffer
import mpt.lab.three.syntax.{SyntRule, Types}
import Types.TLexem
import org.dyndns.phpusr.log.Logger

/**
 * @author phpusr
 *         Date: 23.05.14
 *         Time: 16:43
 */

/**
 * Синтаксический стек
 */
class TSymbStack {

  /** Символы */
  private val items = ListBuffer[TSymbol]()

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

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
  def topLexem: Option[TLexem] = {
    val find = items.reverse.find(_.symbType == TSymbKind.SymbLex)
    if (find.isDefined) {
      Some(find.get.lexem)
    } else None
  }

  /** Удаление элемента из стека */
  def delete(index: Int) = items.remove(index)

  /** Свертка и помещение нового символа на вершину стека */
  def makeTopSymb: Option[TSymbol] = {

    /** Массив хранения символов правила */
    val symbArr = ListBuffer[TSymbol]()

    /** Счетчик символов в стеке */
    var i = 0

    /** Список элементов правила */
    var sRuleList = ListBuffer[String]()

    /** Текущий символ */
    var symCur: TSymbol = null

    val addToRule = (sStr: String, sym: TSymbol) => {
      symCur = sym
      symbArr += getSymbol(i)
      sRuleList += sStr
      delete(i)
    }

    i = items.size - 1
    var break = false
    while (i >= 0 && !break) {
      val s = getSymbol(i)

      if (s.symbType == TSymbKind.SymbSynt) {
        addToRule(s.symbolStr, symCur)
      } else {
        if (symCur == null) {
          addToRule(s.lexem.valueWithReplace, s)
        } else {
          val rowIndex = s.lexem.index
          val columnIndex = symCur.lexem.index
          if (SyntRule.GrammMatrix(rowIndex)(columnIndex) == SyntRule.Basis) {
            addToRule(s.lexem.valueWithReplace, s)
          } else {
            break = true
          }
        }
      }

      if (symbArr.size > SyntRule.RuleLength) break = true

      i -= 1
    }

    var symbol: Option[TSymbol] = None

    // Если выбран хотя бы один символ из стека
    if (symbArr.size > 0) {
      val sRuleStr = sRuleList.reverse.mkString("|")
      logger.debug("\tsRule: " + sRuleStr)
      val find = SyntRule.GrammRules.find(_.mkString("|") == sRuleStr)
      if (find.isDefined) {
        symbol = Some(TSymbol.createSymb(i, symbArr.size, symbArr)) //TODO вместо i д.б. № правила замены
        items += symbol.get
      } else {
        symbArr.clear()
      }
    }

    symbol
  }

  /** Кол-во элементов в стеке */
  def count = items.size

  override def toString = items.map(_.symbolStr).mkString(" | ")

}
