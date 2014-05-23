package mpt.lab.three

import mpt.lab.three.Types.TLexem
import mpt.lab.three.TSymbKind.TSymbKind

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
      lexem.lexInfo.info.get
    }
  }

  /** Тип символа */
  def symbType = symbInfo.symbType

  /** Ссылка на лексему для терминального символа */
  def lexem = symbInfo.lexOne

}

object TSymbol {

  /** Создание терминального символа по лексеме */
  def createLex(lex: TLexem) = {
    new TSymbol {
      symbInfo = new TSymbInfo(lex, null, TSymbKind.SymbLex)
      // Правило не используется пэ 0
      iRuleNum = 0
    }
  }

  /** Создание нетерминального символа на основе правила и массива символов */
  def createSymb(iR: Int, iSymbN: Int, symbArr: Array[TSymbol]) = {
    new TSymbol {
      val lexList = symbArr.reverse.toList
      symbInfo = new TSymbInfo(null, lexList, TSymbKind.SymbSynt)
      // Запоминаем номер правила
      iRuleNum = iR
    }
  }

}

/**
 * Структура данных для символа грамматики
 */
case class TSymbInfo(lexOne: TLexem, lexList: List[TSymbol], symbType: TSymbKind)

/**
 * Типы символов: терминальные (лексемы) и нетерминальные
 */
object TSymbKind extends Enumeration {
  type TSymbKind = Value

  val SymbLex = Value
  val SymbSynt = Value
}

