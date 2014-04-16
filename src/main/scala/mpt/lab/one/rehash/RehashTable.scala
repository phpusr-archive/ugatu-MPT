package mpt.lab.one.rehash

import scala.collection.mutable.ArrayBuffer
import mpt.lab.one.rehash.IndexType.Index
import scala.annotation.tailrec
import mpt.lab.one.stat.Stat

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 17:05
 */

/**
 * Элемент таблицы
 */
case class Node(name: String)

/**
 * Тип индексов элементов таблицы
 */
object IndexType {
  type Index = Int
  val Zero = 0
}

/**
 * Организация таблиц идентификаторов
 * Метод: Простое рехэширование
 */
class RehashTable(tableSize: Index) {
  
  /** Хранилище элементов таблицы */
  private val MinIndex = IndexType.Zero
  private val hashTable = new ArrayBuffer[Node](tableSize)

  /** Статистика добавления элементов */
  private val addStat = new Stat
  /** Статистика поиска элементов */
  private val findStat = new Stat
  /** Возврат результатов статистики */
  def getStat = (addStat.avg(), findStat.avg())

  //Инициализация
  clear()

  /** Добавление элемента в таблицу */
  def add(id: String) {
    // Если в таблице есть хоть одно свободное место
    if (addStat.elementsCount < tableSize) {
      // Изменить статистику и добавить элемент
      addStat.newElement()
      val idHash = getHash(id)
      addRec(id, idHash)
    } else {
      println(">> Table is full") //TODO
    }
  }

  /** Рекурсивный поиск свободной ячейки и добавление туда */
  //TODO add stat
  @tailrec
  private def addRec(id: String, hash: Index) {
    // Инкремент счетчика кол-ва итераций добавления элемента
    addStat.inc()

    // Элемент таблицы под индексом == hash
    val el = hashTable(hash)

    if (el == null) {
      // Если ячейка пуста, то добавляем туда
      hashTable(hash) = Node(id)
    } else if (el.name == id) {
      // Если в ячейке уже есть данный Id, то выводим оообщение
      println(">> Already exists!") //TODO
    } else {
      // Иначе запуск этой же функции с другим хэшем
      val newHash = (hash + 1) % tableSize //TODO вынести
      addRec(id, newHash)
    }
  }
  
  /** Поиск элемента в таблице по имени */
  def find(name: String) = {
    // Инкремент счетчика кол-ва поисков
    findStat.newElement()
    // Рекурсивный поиск элемента
    findRec(name, hashTable.head, hashTable.tail)
  }

  /** Рекурсионный поиск элемента в таблице по имени */
  def findRec(name: String, el: Node, list: Seq[Node]): Option[Node] = {
    // Инкремент счетчика кол-ва итераций поиска элемента
    findStat.inc()
    // Если найден, то возвращаем его
    if (el.name == name) Some(el)
    // Если список пустой, то возвратить None
    else if (list.isEmpty) None
    // Иначе запуск этой же функции, со списком без головы
    else findRec(name, list.head, list.tail)
  }
  
  /** Очистка таблицы */
  def clear() = {
    hashTable.clear()
    for (i <- MinIndex until tableSize) hashTable += null
  }
  
  /** Возврат таблицы идентификаторов */
  def getHashTable = {
    for (index <- MinIndex until tableSize if hashTable(index) != null) yield s"$index: ${hashTable(index)}"
  }
  
  /** 
   * Хэш-функция
   * (Сумма всех символов числа) mod Размер таблицы 
   */
  private def getHash(string: String) = string.getBytes.sum.abs % tableSize
  
}
