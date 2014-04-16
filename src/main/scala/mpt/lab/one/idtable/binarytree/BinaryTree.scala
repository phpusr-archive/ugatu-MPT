package mpt.lab.one.idtable.binarytree

import mpt.lab.one.idtable.{NodeAbstract, IdTableAbstract}

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 21:28
 */

/**
 * Организация таблиц идентификаторов
 * Метод: Бинарное дерево
 */
class BinaryTree extends IdTableAbstract {

  /** Возврат таблицы идентификаторов */
  override def getHashTable: Seq[String] = ???

  /** Поиск элемента в таблице по имени */
  override def find(idName: String): Option[NodeAbstract] = ???

  /** Добавление элемента в таблицу */
  override def add(idName: String): Option[NodeAbstract] = ???

  /** Инициализация таблицы идентификаторов */
  override def init(): Unit = ???

}
