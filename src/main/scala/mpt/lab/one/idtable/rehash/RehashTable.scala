package mpt.lab.one.idtable.rehash

import scala.collection.mutable.ArrayBuffer
import mpt.lab.one.idtable.{NodeAbstract, IdTableAbstract, IndexType}
import IndexType.Index
import scala.annotation.tailrec

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 17:05
 */

/**
 * Элемент таблицы
 */
case class Node(name: String) extends NodeAbstract(name)

/**
 * Организация таблиц идентификаторов
 * Метод: Простое рехэширование
 */
class RehashTable(tableSize: Index) extends IdTableAbstract {

  /** Минимальный индекс таблицы */
  private val MinIndex = IndexType.Zero

  /** Хранилище элементов таблицы */
  private val hashTable = new ArrayBuffer[Node](tableSize)

  ///////////////////////////////////////////

  init()

  ///////////////////////////////////////////

  /** Инициализация */
  override def init() = clear()

  /** Добавление элемента в таблицу */
  override def add(idName: String) = {
    // Если в таблице есть хоть одно свободное место
    if (addStat.elementsCount < tableSize) {
      // Изменить статистику и добавить элемент
      addStat.newElement()
      val idHash = getHash(idName)
      addRec(idName, idHash)
    } else {
      // Если нет, то выдать сообщение и возвратить None
      println(">> Table is full") //TODO
      None
    }
  }

  /** Рекурсивный поиск свободной ячейки и добавление туда */
  @tailrec
  private def addRec(idName: String, hash: Index): Option[Node] = {
    // Инкремент счетчика кол-ва итераций добавления элемента
    addStat.inc()

    // Элемент таблицы под индексом == hash
    val el = hashTable(hash)

    if (el == null) {
      // Если ячейка пуста, то добавляем туда
      val node = Node(idName)
      hashTable(hash) = node
      Some(node)
    } else if (el.name == idName) {
      // Если в ячейке уже есть данный Id, то выводим оообщение
      println(">> Already exists!") //TODO
      None
    } else {
      // Иначе запуск этой же функции с другим хэшем
      val newHash = rehash(hash)
      addRec(idName, newHash)
    }
  }

  /** Функция рехэширования */
  private def rehash(hash: Index) = (hash + 1) % tableSize
  
  /** Поиск элемента в таблице по имени */
  override def find(idName: String) = {
    // Инкремент счетчика кол-ва поисков
    findStat.newElement()
    // Рекурсивный поиск элемента
    findRec(idName, hashTable.head, hashTable.tail)
  }

  /** Рекурсионный поиск элемента в таблице по имени */
  private def findRec(idName: String, el: Node, list: Seq[Node]): Option[Node] = {
    // Инкремент счетчика кол-ва итераций поиска элемента
    findStat.inc()
    // Если найден, то возвращаем его
    if (el.name == idName) Some(el)
    // Если список пустой, то возвратить None
    else if (list.isEmpty) None
    // Иначе запуск этой же функции, со списком без головы
    else findRec(idName, list.head, list.tail)
  }
  
  /** Очистка таблицы */
  def clear() = {
    hashTable.clear()
    for (i <- MinIndex until tableSize) hashTable += null
  }
  
  /** Возврат таблицы идентификаторов */
  override def getHashTable = {
    for (index <- MinIndex until tableSize if hashTable(index) != null) yield s"$index: ${hashTable(index)}"
  }
  
  /** 
   * Хэш-функция
   * (Сумма всех символов числа) mod Размер таблицы 
   */
  private def getHash(string: String) = string.getBytes.sum.abs % tableSize
  
}
