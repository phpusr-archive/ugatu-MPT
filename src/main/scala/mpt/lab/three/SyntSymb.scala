package mpt.lab.three

import mpt.lab.three.Types.TLexem
import mpt.lab.three.TSymbKind.TSymbKind
import mpt.lab.two.lexem.{Position, LexElem}
import scala.collection.mutable.ListBuffer

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

  /** Сдвиг-свертка */
  def buildSyntList(listLex: List[TLexem], symbStack: TSymbStack): TSymbol = {
    var iCnt = listLex.size - 1
    val lexStop = LexElem.createInfo("Начало строки", new Position(0, 0, 0))

    symbStack.push(lexStop)
    var i = 0

    while (i <= iCnt) {
      val lexTCur = symbStack.topLexem.get.lexInfo

      //TODO continue
    }
  }

}


/** Массив символов, состовляющих правило грамматики */
class TSymbArray {
  val data = new Array[TSymbol](SyntSymb.RuleLength)
}

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
          if (Matrix.GrammMatrix(rowIndex)(columnIndex) == Matrix.Equals) {
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
      val find = Matrix.GrammRules.find(_ == sRuleStr)
      if (find.isDefined) {
        symbol = Some(TSymbol.createSymb(i, iSymbN, symbArr))
        items += symbol.get
      } else {
        for (i <- 0 until iSymbN) symbArr(i) = null
      }
    }

    symbol
  }



}


