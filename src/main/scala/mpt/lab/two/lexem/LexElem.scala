package mpt.lab.two.lexem

import mpt.lab.one.idtable.NodeAbstract
import mpt.lab.three.syntax.{SyntSymb, SyntRule}

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
      case Assignment => lexInfo.info.get
      case Equals => lexInfo.info.get
      case Splitter => lexInfo.info.get
      case Info => lexInfo.info.get
    }

    _value.toString
  }

  /** Значение лексемы с заменой переменных и констант */
  def valueWithReplace = {
    lexInfo.name match {
      case LexType.Var => SyntRule.A
      case LexType.Const => SyntRule.E
      case _ => value
    }
  }

  /** Индекс для получения значения в матрице операторного предшествования */
  def index = {
    valueWithReplace match {
      case ";" => 0
      case "if" => 1
      case "then" => 2
      case "else" => 3
      case SyntRule.E | SyntRule.A => 4
      case ":=" => 5
      case "<" => 6
      case ">" => 7
      case "==" => 8
      case "(" => 9
      case ")" => 10
      case SyntSymb.Start => 11 //TODO Начало строки и конец строки
      case SyntSymb.Stop => 11
    }
  }
}


/**
 * Информация о позиции лексемы в тексте входной программы
 *
 * @param line Номер строки, где встретилась лексема
 * @param column Позиция лексемы в строке
 * @param fromBegin Позиция лексемы относительно начала входного файла
 */
case class Position(line: Int, column: Int, fromBegin: Int) {
  override def toString = s"line: ${line+1}, column: ${column+1}, from begin: ${fromBegin+1}"
}


/**
 * Создание лексем
 */
object LexElem {

  /** Создание лексемы типа "переменная" */
  val createVar = (variable: String, position: Position, varInfo: Option[NodeAbstract]) => {
    val lexType = LexType(LexType.Var, None)
    LexElem(lexType, varInfo, None, None, position)
  }

  /** Создание лексемы типа "ключевое слово" */
  val createKeyWord = (lexType: LexType, position: Position) => {
    LexElem(lexType, None, None, None, position)
  }

  /** Создание лексемы типа "константа" */
  val createConst = (const: String, position: Position, isReal: Boolean) => {
    val lexType = LexType(LexType.Const, None)
    val number: AnyVal = if (isReal) const.toFloat else const.toInt
    LexElem(lexType, None, Some(number), None, position)
  }

  /** Создание лексемы типа "оператор" */
  val createOperator = (lexType: LexType, position: Position) => {
    LexElem(lexType, None, None, None, position)
  }

  /** Создание информационной лексемы */
  val createInfo = (message:String, position: Position) => {
    val posString = s"$message: $position"
    val lexType = LexType(LexType.Info, Some(posString))
    LexElem(lexType, None, None, None, position)
  }

  /** Создание лексемы-разделителя */
  val createKey = (key: String, position: Position) => {
    val lexType = LexType(LexType.Splitter, Some(key))
    LexElem(lexType, None, None, None, position)
  }

}
