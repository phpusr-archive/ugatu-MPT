package mpt.lab.two.automat

import mpt.lab.two.lexem.{LexOperators, LexKeyWord, LexElem, Position}
import org.dyndns.phpusr.log.Logger
import scala.collection.mutable.ListBuffer
import mpt.lab.one.idtable.rehash.RehashTable

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 13:18
 */

/**
 * Mixin - позволяет добавлять элементы в список
 */
trait ElementAdder {

  /** Логирование */
  private val logger = Logger(infoEnable = false, debugEnable = false, traceEnable = false)

  /** Таблица найденных лексем */
  protected var lexList: ListBuffer[LexElem] = null

  /** Таблица переменных */
  private val varTable = new RehashTable(1000)

  // Позиция разбора
  protected var lineIndex = 0
  protected var columnIndex = 0
  protected var fromBeginIndex = 0  


  ////////////////////////////////////////////////////////


  /** Текущая позиция разбора файла */
  protected def currentPosition = Position(lineIndex, columnIndex, fromBeginIndex)

  /** Добавление лексемы типа "слово" в таблицу лексем */
  protected def addWordToList(word: String) {
    val keyWord = LexKeyWord.getKeyWordByName(word)
    
    if (keyWord.isDefined) {
      logger.debug(s"\t add key word: '$word'")
      lexList += LexElem.createKeyWord(keyWord.get, currentPosition)
    } else {
      logger.debug(s"\t add var: '$word'")  
      val varInfo = varTable.add(word)
      lexList += LexElem.createVar(word, currentPosition, varInfo)
    }
  }

  /** Добавление лексемы типа "слово" и типа "разделитель" в таблицу лексем */
  protected def addWordKeyToList(id: String, key: String) = {
    addWordToList(id)
    addKeyToList(key)
  }

  /** Добавление лексемы типа "константа" в таблицу лексем */
  protected def addConstToList(const: String, isReal: Boolean = false) {
    logger.debug(s"\t add const: '$const'")
    lexList += LexElem.createConst(const, currentPosition, isReal)
  }

  /** Добавление лексемы типа "константа" и типа "разделитель" в таблицу лексем */
  protected def addConstKeyToList(const: String, key: String, isReal: Boolean = false) {
    addConstToList(const, isReal)
    addKeyToList(key)
  }

  /** Добавление лексемы типа "ключевое слово" или "разделитель" в таблицу лексем */
  protected def addKeyToList(key: String) {
    logger.debug(s"\t add key: '$key'")
    lexList += LexElem.createKey(key, currentPosition)
  }

  /** Добавление лексемы типа "ключевое слово" и "разделитель" в таблицу лексем подряд */
  protected def add2KeysToList() = ???

  /** Добавление лексемы типа "оператор" в таблицу лексем */
  protected def addOperatorToList(operator: String) {
    logger.debug(s"\t add operator: '$operator'")
    val oper = LexOperators.getOperatorByName(operator)
    lexList += LexElem.createOperator(oper, currentPosition)
  }

  /** Добавление лексемы типа "оператор" и "разделитель" в таблицу лексем */
  protected def addOperatorKeyToList(operator: String, key: String) {
    addOperatorToList(operator)
    addKeyToList(key)
  }

  /** Добавление информационной лексемы в таблицу лексем */
  protected def addInfoToList(message: String) {
    logger.debug(s"\t info: $message")
    lexList += LexElem.createInfo(message, currentPosition)
  }


}
