package mpt.lab.three

import mpt.lab.three.Types.TLexem
import mpt.lab.three.TSymbKind.TSymbKind
import mpt.lab.two.lexem.{LexType, Position, LexElem}

/**
 * @author phpusr
 *         Date: 22.05.14
 *         Time: 11:58
 */

/**
 * Типы символов: терминальные (лексемы) и нетерминальные
 */
object TSymbKind extends Enumeration {
  type TSymbKind = Value

  val SymbLex = Value
  val SymbSynt = Value
}

object Types {
  type TLexem = LexElem
}

/**
 * Структура данных для символа грамматики
 */
case class TSymbInfo(lexOne: TLexem, lexList: List[TSymbol], symbType: TSymbKind)



/**
 * Модуль, обеспечивающий выполнение функции синтаксического
 * разобра с помощью алгоритма "сдвиг-свертка"
 */
object SyntSymb {

  val RuleLength = ???

  val LexStart: LexType = ???

  /** Сдвиг-свертка */
  def buildSyntList(listLex: List[TLexem], symbStack: TSymbStack): TSymbol = {
    val lexStop = LexElem.createInfo("Начало файла", new Position(0, 0, 0))
    symbStack.push(lexStop)

    val iCnt = listLex.size - 1
    var result: TSymbol = null

    /** Прерывание цикла */
    var break = false

    var i = 0
    while (i <= iCnt && !break) {
      val lexTCur = symbStack.topLexem.get.lexInfo
      val lexCurFromList = listLex(i)

      if (lexTCur == LexStart && lexCurFromList.lexInfo == LexStart) {
        break = true
      } else {
        // Смотрим отношение лексемы на вершине стека и текущей лексемы в строке
        var cRule = Matrix.GrammMatrix(lexTCur)(lexCurFromList.lexInfo)
        cRule = correctRule(cRule, lexTCur, lexCurFromList.lexInfo, symbStack)

        cRule match {
          case '<' | '=' => // Надо выполнить сдвиг (перенос)
            symbStack.push(lexCurFromList)
            i += 1
          case '>' => // Надо выполнить свертку
            if (symbStack.makeTopSymb.isEmpty) {
              // Если не удалось выполнить свертку
              result = TSymbol.createLex(lexCurFromList)
              break = true
            } else {
              // Отношение не установлено - ошибка разбора
              result = TSymbol.createLex(lexCurFromList)
              break = true
            }
        }
      }
    }

    if (result == null) {
      if (symbStack.count == 2) {
        result = symbStack.getSymbol(1)
      } else {
        result = TSymbol.createLex(listLex(iCnt))
      }
    }

    result
  }

  /** Корректировка отношения */
  private def correctRule(cRule: Char, lexTCur: LexType, lex: LexType, symbStack: TSymbStack): Char = cRule

}
