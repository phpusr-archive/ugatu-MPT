package mpt.lab.two.automat

import mpt.lab.two.lexem.{LexKeyWord, LexElem, Position}
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
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Таблица найденных лексем */
  protected var lexList: ListBuffer[LexElem] = null

  /** Таблица переменных */
  private val varTable = new RehashTable(1000)


  ////////////////////////////////////////////////////////


  private def currentPosition = Position(0, 0, 0) //TODO

  /** Добавление лексемы типа "слово" в таблицу лексем */
  protected def addWordToList(word: String) {
    val keyWord = LexKeyWord.getKeyWordByName(word)
    
    if (keyWord.isDefined) {
      logger.debug(s"\t add key word: '$word'")
      lexList += LexElem.createKeyWord(keyWord.get, word, currentPosition)
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
  protected def addConstToList(const: String) {
    logger.debug(s"\t add const: '$const'")
    lexList += LexElem.createConst(const, currentPosition)
  }

  /** Добавление лексемы типа "константа" и типа "разделитель" в таблицу лексем */
  protected def addConstKeyToList(const: String, key: String) {
    addConstToList(const)
    addKeyToList(key)
  }

  /** Добавление лексемы типа "ключевое слово" или "разделитель" в таблицу лексем */
  protected def addKeyToList(key: String) {
    logger.debug(s"\t add key: '$key'")
    lexList += LexElem.createKey(key, currentPosition)
  }

  /** Добавление лексемы типа "ключевое слово" и "разделитель" в таблицу лексем подряд */
  protected def add2KeysToList() = ???

}
