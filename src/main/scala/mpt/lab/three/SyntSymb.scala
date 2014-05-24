package mpt.lab.three

import mpt.lab.three.Types.TLexem
import mpt.lab.two.lexem.{LexType, Position, LexElem}

/**
 * @author phpusr
 *         Date: 22.05.14
 *         Time: 11:58
 */

object Types {
  type TLexem = LexElem
}


/**
 * Модуль, обеспечивающий выполнение функции синтаксического
 * разобра с помощью алгоритма "сдвиг-свертка"
 */
object SyntSymb {

  val RuleLength = ??? //TODO

  val LexStart: LexType = ??? //TODO

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
      val lexTCur = symbStack.topLexem.get
      val lexCurFromList = listLex(i)

      // Если на вершине стека начальная лексема, а текущая лексема - конечная, то разбор завершен
      if (lexTCur.lexInfo == LexStart && lexCurFromList.lexInfo == LexStart) {
        break = true
      } else {
        // Смотрим отношение лексемы на вершине стека и текущей лексемы в строке
        var cRule = SyntRule.GrammMatrix(lexTCur.index)(lexCurFromList.index)
        cRule = SyntRule.correctRule(cRule, lexTCur, lexCurFromList.lexInfo, symbStack)

        cRule match {
          case '<' | '=' => // Надо выполнить сдвиг (перенос)
            symbStack.push(lexCurFromList)
            i += 1
          case '>' => // Надо выполнить свертку
            if (symbStack.makeTopSymb.isEmpty) {
              // Если не удалось выполнить свертку
              result = TSymbol.createLex(lexCurFromList)
              break = true
            }
          case _ => // Отношение не установлено - ошибка разбора
            result = TSymbol.createLex(lexCurFromList)
            break = true
        }
      }
    }

    if (result == null) { // Если разбор прошел без ошибок
      if (symbStack.count == 2) {
        result = symbStack.getSymbol(1)
      } else {
        result = TSymbol.createLex(listLex(iCnt))
      }
    }

    result
  }

}
