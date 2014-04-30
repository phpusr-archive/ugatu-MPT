package mpt.lab.two.automat

import scala.collection.mutable.ListBuffer
import mpt.lab.two.automat.AutoPos.AutoPos
import scala.util.matching.Regex
import mpt.lab.two.lexem.{Position, LexElem}
import org.dyndns.phpusr.log.Logger

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:27
 */

/**
 * Класс - моделирующий работу КА, на основе которого построен лексический распознаватель
 */
class LexAuto {

  /** Синтаксический анализ выполнен без ошибок */
  private val NoErrors = 0

  /** Текущее состояние автомата */
  var currentState = AutoPos.H

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)
  
  /** Таблица найденных лексем */
  private var lexList: ListBuffer[LexElem] = null

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
    // Инициализация
    init(listLex)

    for (lineIndex <- 0 until lines.size) {

      // Обрабатываемая строка
      val line = lines(lineIndex)

      for (charIndex <- 0 until line.size) {

        // Обрабатываемый символ //TODO обработка конца строки
        val char = line(charIndex).toString
        logger.debug(s"char: '$char'")

        import mpt.lab.two.automat.AutoPos._
        currentState match {
          case H => hState(char)
          case C => cState(char)
          case G => ???
          case V => ???
          case D => ???
        }
      }
    }

    NoErrors
  }

  /** Сброс состояния автомата */
  private def reset() {
    currentState = AutoPos.H
  }

  /** Обработка начального состояния */
  private def hState(char: String) {
    char match {
      // Если начало комментария
      case "{" => changeCurrentState(AutoPos.C)

      // Если начало знака присваивания
      case ":" => changeCurrentState(AutoPos.G)

      // Если незначащий символ
      case _ => if (isListened(char, "(,);") || isWhitespace(char)) {
        addKeyToList(char)
        changeCurrentState(AutoPos.F)
      } else if (isLetter(char, "iteoxa")) {
        // Если буква, за исключением (i,t,e,o,x,a)
        changeCurrentState(AutoPos.V)
      } else if (isDigit(char)) {
        // Если цифра
        changeCurrentState(AutoPos.D)
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

      case _ => if (isAnyChar(char, "}")) {
        logger.debug(s"Comment char: $char")
      } else {
        // Что-то еще
        notSupportCase(char)
      }
    }
  }

  /** Не поддерживаемая операция */
  private def notSupportCase(char: String) {
    throw new MatchError(s"Not support case: '$char'")
  }

  /** Меняет текущее состояние автомата */
  private def changeCurrentState(newState: AutoPos) {
    currentState = newState
    logger.debug(s"\tchange state: $newState")
  }

  /** Строка состоит из переданных символов */
  private def isListened(string: String, includeChars: String) = {
    val splitterRegex = new Regex(s"^[$includeChars]+$$")

    splitterRegex.findFirstIn(string).isDefined
  }


  /** Строка состоит из незначащих символов */
  private def isWhitespace(string: String) = {
    val whitespaceRegex = new Regex("^\\s+$")

    whitespaceRegex.findFirstIn(string).isDefined
  }

  /** Строка состоит из букв и не включает $excludeChars */
  private def isLetter(string: String, excludeChars: String) = {
    val letterRegex = new Regex("^[a-zA-Z_]+$")
    val excludeRegex = new Regex(s"[$excludeChars]")

    val isLetter = letterRegex.findFirstIn(string).isDefined
    val isNotExclude = excludeRegex.findFirstIn(string).isEmpty

    isLetter && isNotExclude
  }

  /** Строка состоит из цифр */
  private def isDigit(string: String) = {
    val digitRegex = new Regex("^\\d+$")

    digitRegex.findFirstIn(string).isDefined
  }

  /** Строка состоит из любых символов кроме $excludeChars */
  private def isAnyChar(string: String, excludeChars: String) = {
    val anyCharRegex = new Regex("^.+$")
    val excludeRegex = new Regex(s"[$excludeChars]")

    val isAnyChar = anyCharRegex.findFirstIn(string).isDefined
    val isNotExclude = excludeRegex.findFirstIn(string).isEmpty

    isAnyChar && isNotExclude
  }

  private def currentPosition = Position(0, 0, 0) //TODO

  /** Добавление лексемы типа "переменная" в таблицу лексем */
  private def addVarToList() = ???

  /** Добавление лексемы типа "переменная" и типа "разделитель" в таблицу лексем */
  private def addVarKeyToList() = ???

  /** Добавление лексемы типа "константа" в таблицу лексем */
  private def addConstToList() = ???

  /** Добавление лексемы типа "константа" и типа "разделитель" в таблицу лексем */
  private def addConstKeyToList() = ???

  /** Добавление лексемы типа "ключевое слово" или "разделитель" в таблицу лексем */
  private def addKeyToList(key: String) = {
    logger.debug(s"\tadd key: '$key'")
    lexList += LexElem.createKey(key, currentPosition)
  }

  /** Добавление лексемы типа "ключевое слово" и "разделитель" в таблицу лексем подряд */
  private def add2KeysToList() = ???
  
}
