package mpt.lab.three

import mpt.lab.three.Types.TLexem

/**
 * @author phpusr
 *         Date: 23.05.14
 *         Time: 16:40
 */

/**
 * Предварительное описание класса "Символ"
 */
class TSymbol {

  /** Информация о символе */
  private var symbInfo: TSymbInfo = null

  /** Номер правила, которым создан символ */
  private var iRuleNum: Int = 0

  /** Получение символа из правила по номеру символа */
  def getItem(symbolIndex: Int): TSymbol = symbInfo.lexList(symbolIndex)

  /** Получение кол-ва символов в правиле */
  def count: Int = symbInfo.lexList.size

  /** Формирование строкового представления символа */
  def symbolStr: String = {
    if (symbType == TSymbKind.SymbSynt) {
      makeSymbolStr(iRuleNum)
    } else {
      lexem.lexInfo.info.get
    }
  }

  def makeSymbolStr(ruleNum: Int): String = ???

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

