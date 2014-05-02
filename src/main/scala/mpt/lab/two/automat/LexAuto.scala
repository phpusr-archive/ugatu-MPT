package mpt.lab.two.automat

import scala.collection.mutable.ListBuffer
import mpt.lab.two.lexem.{LexOperators, LexElem}
import org.dyndns.phpusr.log.Logger
import mpt.lab.two.automat.StringMatcher._

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:27
 */

object LexAuto {
  /** Синтаксический анализ выполнен без ошибок */
  val NoErrors = -1
}

/**
 * Класс - моделирующий работу КА, на основе которого построен лексический распознаватель
 */
class LexAuto extends ElementAdder {

  /** Дополнитель конца строки */
  private val LineEnd = " "

  ////////////////////////////////////////////////////////

  /** Текущее состояние автомата */
  var currentState = AutoPos.H
  /** Предыдущее состояние автомата (для отладки) */
  var prevState = AutoPos.H

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Текущее имя идентификатора */
  private var currentIdName = ""

  /** Текущая числовая константа */
  private var currentNumberConst = ""


  ////////////////////////////////////////////////////////


  /**  Инициализация */
  private def init(listLex: ListBuffer[LexElem]) {
    this.lexList = listLex
    reset()
  }

  /**
   * Моделирование работы КА
   * @param lines Текст программы
   * @param listLex Таблица найденных лексем
   * @return 0 если лексический анализ выполнен без ошибок,
   *         а если ошибка обнаружена - номер строки в исходном файле,
   *         в которой она присутствует
   */
  def makeLexList(lines: Array[String], listLex: ListBuffer[LexElem]): Int = {
    var char = ""

    try {
      // Инициализация
      init(listLex)

      lines.foreach { l =>
        columnIndex = 0

        // Обрабатываемая строка
        val line = lines(lineIndex) + LineEnd

        line.foreach { c =>
          // Обрабатываемый символ
          char = c.toString
          logger.debug(s"char: '$char'")

          import mpt.lab.two.automat.AutoPos._
          currentState match {
            case H | F => hState(char)
            case C => cState(char)
            case G => gState(char)
            case V => vState(char)
            case D => dState(char)
            case E => eState(char)
            case L => lState(char)
            case GR => grState(char)
            case P => pState(char)
          }

          columnIndex += 1
          fromBeginIndex += 1
        }

        lineIndex += 1
      }

      LexAuto.NoErrors
    } catch {
      case e: MatchError =>
        addInfoToList(s"Ошибка при разборе '$char'")
        currentPosition.fromBegin
    }

  }

  /** Сброс состояния автомата */
  private def reset() {
    lineIndex = 0
    columnIndex = 0
    fromBeginIndex = 0
    currentState = AutoPos.H
  }

  /** Обработка начального состояния */
  private def hState(char: String) {
    char match {
      // Если начало комментария
      case "{" => changeCurrentState(AutoPos.C)

      // Если начало знака присваивания
      case ":" => changeCurrentState(AutoPos.G)

      // Если оператор сравнения
      case "=" => changeCurrentState(AutoPos.E)
      case "<" => changeCurrentState(AutoPos.L)
      case ">" => changeCurrentState(AutoPos.GR)

      // Если символ-разделитель
      case "(" | "," | ")" | ";" =>
        addKeyToList(char)
        changeCurrentState(AutoPos.F)

      // Если незначащий символ
      case _ => if (isWhitespace(char)) {
        changeCurrentState(AutoPos.F)
      } else if (isLetter(char)) {
        // Если буква, за исключением (i,t,e,o,x,a)
        changeCurrentState(AutoPos.V)
        currentIdName = char
      } else if (isDigit(char)) {
        // Если цифра
        changeCurrentState(AutoPos.D)
        currentNumberConst = char
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка комментария */
  private def cState(char: String) {
    char match {
      // Конец комментария
      case "}" => changeCurrentState(AutoPos.F)

      // Текст комментария
      case _ => if (isAnyChar(char, "}")) {
        logger.debug(s"Comment char: $char")
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка знака присваивания */
  private def gState(char: String) {
    char match {
      // Конец знака присваивания
      case "=" =>
        addOperatorToList(LexOperators.Assignment)
        changeCurrentState(AutoPos.F)

      // Что-то еще
      case _ => notSupportCase(char)
    }
  }

  /** Обработка идентификатора */
  private def vState(char: String) {
    char match {
      // Если начало комментария
      case "{" =>
        addWordToList(currentIdName)
        changeCurrentState(AutoPos.C)

      // Если начало знака присваивания
      case ":" =>
        addWordToList(currentIdName)
        changeCurrentState(AutoPos.G)

      // Если буквы и цифры
      case _ => if (isLetter(char) || isDigit(char)) {
        currentIdName += char
        logger.debug(s"\t new name: $currentIdName")
      } else if (isWhitespace(char)) {
        // Если незначащий символ
        addWordToList(currentIdName)
        changeCurrentState(AutoPos.F)
      } else if (isListened(char, "(,).;")) {
        // Если символ-разделитель
        addWordKeyToList(currentIdName, char)
        changeCurrentState(AutoPos.F)
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка числовых констант */
  private def dState(char: String) {
    char match {
      // Если начало комментария
      case "{" =>
        addConstToList(currentNumberConst)
        changeCurrentState(AutoPos.C)

      // Если начало знака присваивания
      case ":" =>
        addConstToList(currentNumberConst)
        changeCurrentState(AutoPos.G)

      // Если разделитель вещественного числа
      case "." =>
        changeCurrentState(AutoPos.P)
        currentNumberConst += char

      // Если цифры
      case _ => if (isDigit(char)) {
        currentNumberConst += char
        logger.debug(s"\t new digit: $currentNumberConst")
      } else if (isWhitespace(char)) {
        // Если незначащий символ
        addConstToList(currentNumberConst)
        changeCurrentState(AutoPos.F)
      } else if (isListened(char, "(,);")) {
        // Если символ-разделитель
        addConstKeyToList(currentNumberConst, char)
        changeCurrentState(AutoPos.F)
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка вещественных чисел */
  private def pState(char: String) {
    val isReal = true
    char match {
      // Если начало комментария
      case "{" =>
        addConstToList(currentNumberConst, isReal)
        changeCurrentState(AutoPos.C)

      // Если начало знака присваивания
      case ":" =>
        addConstToList(currentNumberConst, isReal)
        changeCurrentState(AutoPos.G)

      // Если цифры
      case _ => if (isDigit(char)) {
        currentNumberConst += char
        logger.debug(s"\t new digit: $currentNumberConst")
      } else if (isWhitespace(char)) {
        // Если незначащий символ
        addConstToList(currentNumberConst, isReal)
        changeCurrentState(AutoPos.F)
      } else if (isListened(char, "(,);")) {
        // Если символ-разделитель
        addConstKeyToList(currentNumberConst, char, isReal)
        changeCurrentState(AutoPos.F)
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка оператора сравнения (==) */
  private def eState(char: String) {
    char match {
      // Конец оператора сравнения (==)
      case "=" =>
        addOperatorToList(LexOperators.Equals)
        changeCurrentState(AutoPos.F)

      // Что-то еще
      case _ => notSupportCase(char)
    }
  }

  /** Обработка оператора сравнения (< <=) */
  private def lState(char: String) {
    char match {
      // Конец оператора сравнения (<=)
      case "=" =>
        addOperatorToList(LexOperators.LE)
        changeCurrentState(AutoPos.F)

      // Если незначащий символ
      case _ => if (isWhitespace(char)) {
        addOperatorToList(LexOperators.L)
        changeCurrentState(AutoPos.F)
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Обработка оператора сравнения (> >=) */
  private def grState(char: String) {
    char match {
      // Конец оператора сравнения (>=)
      case "=" =>
        addOperatorToList(LexOperators.GE)
        changeCurrentState(AutoPos.F)

      // Если незначащий символ
      case _ => if (isWhitespace(char)) {
        addOperatorToList(LexOperators.G)
        changeCurrentState(AutoPos.F)
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Не поддерживаемая операция */
  private def notSupportCase(char: String) {
    logger.debug(s"\t not support: $char")
    throw new MatchError(s"Not support case: '$char'")
  }

  /** Меняет текущее состояние автомата */
  private def changeCurrentState(newState: AutoPos) {
    prevState = currentState
    currentState = newState
    logger.debug(s"\t change state: $newState")
  }

}
