package mpt.lab.three.symbol

import mpt.lab.three.syntax.{SyntRule, Types}
import Types.TLexem
import scala.collection.mutable.ListBuffer
import mpt.lab.three.symbol.TSymbKind.TSymbKind

/**
 * @author phpusr
 *         Date: 23.05.14
 *         Time: 16:40
 */

/**
 * Описание всех данных, связанных с понятием "символ грамматики"
 */
class TSymbol {

  /** Информация о символе */
  private var symbInfo: TSymbInfo = null

  /**
   * Номер правила, которым создан символ
   * Для терминальных символов = 0,
   * для нетерминальных символов он может быть от 1 до 13
   */
  private var iRuleNum: Int = 0

  /** Получение символа из правила по номеру символа */
  def getItem(symbolIndex: Int): TSymbol = symbInfo.lexList(symbolIndex)

  /** Получение кол-ва символов в правиле */
  def count: Int = symbInfo.lexList.size

  /** Формирование строкового представления символа */
  def symbolStr: String = {
    if (symbType == TSymbKind.SymbSynt) {
      SyntRule.makeSymbolStr(iRuleNum)
    } else {
      lexem.value
    }
  }

  /** Тип символа */
  def symbType = symbInfo.symbType

  /** Ссылка на лексему для терминального символа */
  def lexem = symbInfo.lexOne

  /** Дочерние элементы */
  def children = symbInfo.lexList

  /** Добавление дочерних элементов */
  def children_=(childrenList: List[TSymbol]) = symbInfo.lexList = childrenList

}

object TSymbol {

  /** Создание терминального символа по лексеме */
  def createLex(lex: TLexem) = {
    new TSymbol {
      symbInfo = new TSymbInfo(TSymbKind.SymbLex, lex, List[TSymbol]())
      // Правило не используется пэ 0
      iRuleNum = 0
    }
  }

  /** Создание нетерминального символа на основе правила и массива символов */
  def createSymb(iR: Int, iSymbN: Int, symbArr: ListBuffer[TSymbol]) = {
    new TSymbol {
      val lexList = symbArr.reverse.toList
      symbInfo = new TSymbInfo(TSymbKind.SymbSynt, null, lexList)
      // Запоминаем номер правила
      iRuleNum = iR
    }
  }

}

/**
 * Структура данных для символа грамматики
 */
case class TSymbInfo(symbType: TSymbKind, lexOne: TLexem, var lexList: List[TSymbol])

/**
 * Типы символов
 */
object TSymbKind extends Enumeration {
  type TSymbKind = Value

  /** Терминальный символ (лексема) */
  val SymbLex = Value

  /** Нетерминальный символ */
  val SymbSynt = Value
}

