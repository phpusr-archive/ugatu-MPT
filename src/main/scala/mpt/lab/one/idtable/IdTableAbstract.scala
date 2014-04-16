package mpt.lab.one.idtable

import mpt.lab.one.stat.Stat
import mpt.lab.one.idtable.IndexType._

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 20:27
 */

/**
 * Тип индексов элементов таблицы
 */
object IndexType {
  type Index = Int
  val Zero = 0
}

/**
 * Абстрактный класс элементов таблицы
 */
abstract class NodeAbstract(name: String)

/**
 * Абстрактная таблица идентификаторов
 */
abstract class IdTableAbstract(tableSize: Index) {

  /** Статистика добавления элементов */
  protected val addStat = new Stat

  /** Статистика поиска элементов */
  protected val findStat = new Stat

  ///////////////////////////////////////////

  // Initialization code here ...

  ///////////////////////////////////////////

  /** Возврат результатов статистики */
  def getStat = (addStat.avg(), findStat.avg())

  /**
   * Хэш-функция
   * (Сумма всех символов числа) mod Размер таблицы
   */
  protected def getHash(string: String) = string.getBytes.sum.abs % tableSize

  /** Инициализация таблицы идентификаторов */
  def init()

  /** Добавление элемента в таблицу */
  def add(idName: String): Option[NodeAbstract]

  /** Поиск элемента в таблице по имени */
  def find(idName: String): Option[NodeAbstract]

  /** Очистка таблицы */
  def clear()

  /** Возврат таблицы идентификаторов */
  def getIdTable: Seq[String]

}
