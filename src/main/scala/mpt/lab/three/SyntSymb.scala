package mpt.lab.three

import mpt.lab.three.Types.TLexem
import mpt.lab.two.lexem.{Position, LexElem}

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

  val RuleLength = 10 //TODO вставить правильное значение

  val Start = "start"
  val LexStart = LexElem.createKey(Start, new Position(0, 0, 0)) //TODO разобраться с названиями

  val Stop = "stop"
  val LexStop = LexElem.createKey(Stop, new Position(0, 0, 0))

  /**
   * Сдвиг-свертка
   * Результат функции:
   * - нетерминальный символ (корень синтаксического дерева), если разбор был выполнен успешно
   * - терминальный символ, ссылающийся на лексему, где была обнаружена ошибка, если разбор выполнен с ошибками
   */
  def buildSyntList(listLex: List[TLexem], symbStack: TSymbStack): TSymbol = {    
    symbStack.push(LexStop)

    val iCnt = listLex.size - 1
    var result: TSymbol = null

    /** Прерывание цикла */
    var break = false

    var i = 0
    while (i <= iCnt && !break) {
      val lexTCur = symbStack.topLexem.get
      val lexCurFromList = listLex(i)

      // Если на вершине стека начальная лексема, а текущая лексема - конечная, то разбор завершен
      if (lexTCur == LexStop && lexCurFromList == LexStart) {
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
