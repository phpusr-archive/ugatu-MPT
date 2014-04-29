package mpt.lab.two

import mpt.lab.one.idtable.rehash.Node

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
case class LexElem(lexInfo: LexType, varInfo: Node, constVal: AnyVal, szInfo: String, pos: Position)


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
  def createVar = () => ???

  /** Создание лексемы типа "константа" */
  def createConst = () => ???

  /** Создание информационной лексемы */
  def createInfo = () => ???

  /** Создание лексемы другого типа */
  def createKey = () => ???

}
