package mpt.lab.two.lexem

import mpt.lab.one.idtable.NodeAbstract

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:00
 */

/**
 * Структура данных таблицы идентификаторов
 *
 * @param lexInfo Информация о лексеме
 * @param varInfo Ссылка на элемент таблицы идентификаторов для лексем типа "переменная"
 * @param constVal Значение для лексем типа "константа"
 * @param szInfo Проивзольная строка для информационной лексемы
 * @param pos Информация о позиции лексемы в тексте входной программы
 */
case class LexElem(lexInfo: LexType, varInfo: Option[NodeAbstract], constVal: Option[AnyVal], szInfo: Option[String], pos: Position) {
  /** Значение лексемы */
  def value = {
    import mpt.lab.two.lexem.LexType._
    val _value = lexInfo.name match {
      case Var => varInfo.get.name
      case Const => constVal.get
      case KeyWord => lexInfo.info.get
      case AssignmentSign => lexInfo.info.get
      case SplitterSign => lexInfo.info.get
      case Info => lexInfo.info.get
    }

    _value.toString
  }
}


/**
 * Информация о позиции лексемы в тексте входной программы
 *
 * @param lineIndex Номер строки, где встретилась лексема
 * @param columnIndex Позиция лексемы в строке
 * @param fromBegin Позиция лексемы относительно начала входного файла
 */
case class Position(lineIndex: Int, columnIndex: Int, fromBegin: Int)


/**
 * Создание лексем
 */
object LexElem {

  /** Создание лексемы типа "переменная" */
  def createVar = (variable: String, position: Position, varInfo: Option[NodeAbstract]) => {
    val lexType = LexType(LexType.Var, None)
    LexElem(lexType, varInfo, None, None, position)
  }

  /** Создание лексемы типа "ключевое слово" */
  def createKeyWord = (lexType: LexType, position: Position) => {
    LexElem(lexType, None, None, None, position)
  }

  /** Создание лексемы типа "константа" */
  def createConst = (const: String, position: Position) => {
    val lexType = LexType(LexType.Const, None)
    //TODO вещественные числа
    val number = const.toInt
    LexElem(lexType, None, Some(number), None, position)
  }

  /** Создание лексемы типа "оператор" */
  def createOperator = (lexType: LexType, position: Position) => {
    LexElem(lexType, None, None, None, position)
  }

  /** Создание информационной лексемы */
  def createInfo = (message:String, position: Position) => {
    val posString = s"$message: line: ${position.lineIndex+1}, column: ${position.columnIndex+1}, from begin: ${position.fromBegin}"
    val lexType = LexType(LexType.Info, Some(posString))
    LexElem(lexType, None, None, None, position)
  }

  /** Создание лексемы другого типа */
  def createKey = (key: String, position: Position) => {
    val lexType = LexType(LexType.SplitterSign, Some(key))
    LexElem(lexType, None, None, None, position)
  }

}
