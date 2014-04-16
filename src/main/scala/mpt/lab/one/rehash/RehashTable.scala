package mpt.lab.one.rehash

import scala.collection.mutable.ArrayBuffer
import mpt.lab.one.rehash.HashType.Hash
import scala.annotation.tailrec

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
 * Тип hash
 */
object HashType {
  type Hash = Int

  val Zero = 0
}

/**
 * Организация таблиц идентификаторов
 * Метод: Простое рехэширование
 */
class RehashTable(tableSize: Hash) {
  
  /** Хранилище элементов таблицы */
  private val MinIndex = HashType.Zero
  private val hashTable = new ArrayBuffer[Node](tableSize)

  //Инициализация
  clear()

  /** Добавление элемента в таблицу */
  def add(id: String) {
    val idHash = getHash(id)
    addRec(id, idHash)
  }

  /** Рекурсивный поиск свободной ячейки и добавление туда */
  //TODO add stat
  @tailrec
  private def addRec(id: String, hash: Hash) {
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
      val newHash = (hash + 1) % tableSize
      addRec(id, newHash)
    }
  }
  
  /** Поиск элемента в таблице по имени */
  def find(name: String) = ???
  
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
