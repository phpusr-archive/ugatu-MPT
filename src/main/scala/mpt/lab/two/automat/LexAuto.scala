package mpt.lab.two.automat

import java.io.File
import scala.collection.mutable.ListBuffer
import scala.io.Source
import mpt.lab.two.automat.AutoPos.AutoPos
import scala.util.matching.Regex
import mpt.lab.two.lexem.{Position, LexElem}

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
  private var currentState = AutoPos.H

  /**
   * Моделирование работы КА
   * @param file Текст программы
   * @param listLex Таблица найденных лексем
   * @return 0 если лексический анализ выполнен без ошибок,
   *         а если ошибка обнаружена - номер строки в исходном файле,
   *         в которой она присутствует
   */
  def makeLexList(file: File, listLex: ListBuffer[LexElem]): Int = {
    val lines = Source.fromFile(file).getLines().toArray

    for (lineIndex <- 0 to lines.size) {

      // Обрабатываемая строка
      val line = lines(lineIndex)

      for (charIndex <- 0 to line.size) {

        // Обрабатываемый символ
        val char = line(charIndex).toString

        import mpt.lab.two.automat.AutoPos._
        currentState match {
          case H => ???
          case C => ???
          case G => ???
          case V => ???
          case D => ???
        }
      }
    }

    NoErrors
  }

  /** Обработка начального состояния */
  private def hState(char: String) {
    char match {
      // Если не значащий символ
      case "(" | "," | ")" | ";" => addKeyToList(char); changeCurrentState(AutoPos.H)
      // Если начало комментария
      case "{" => changeCurrentState(AutoPos.C)
      // Если начало знака присваивания
      case ":" => changeCurrentState(AutoPos.G)
      // Если буква за исключением (i,t,e,o,x,a)
      case _ => if (isLetter(char, "iteoxa")) {
        changeCurrentState(AutoPos.V)
      } else if (isDigit(char)) {
        // Если цифра
        changeCurrentState(AutoPos.D)
      } else {
        // Что-то еще
        throw new MatchError(s"Not support case: $char")
      }
    }
  }

  /** Меняет текущее состояние автомата */
  private def changeCurrentState(newState: AutoPos) {
    currentState = newState
  }

  /** Строка состоит из букв и не включает $excludeChars */
  private def isLetter(string: String, excludeChars: String) = {
    val letterRegex = new Regex("[a-zA-Z_]")
    val excludeRegex = new Regex(s"[$excludeChars]")

    val isLetter = !letterRegex.findAllIn(string).isEmpty
    val isNotExclude = excludeRegex.findAllIn(string).isEmpty

    isLetter && isNotExclude
  }

  /** Строка состоит из цифр */
  private def isDigit(string: String) = {
    val digitRegex = new Regex("\\d")

    !digitRegex.findAllIn(string).isEmpty
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
    LexElem.createKey(key, currentPosition)
  }

  /** Добавление лексемы типа "ключевое слово" и "разделитель" в таблицу лексем подряд */
  private def add2KeysToList() = ???
  
}
