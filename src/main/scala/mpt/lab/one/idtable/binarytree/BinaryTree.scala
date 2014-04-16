package mpt.lab.one.idtable.binarytree

import mpt.lab.one.idtable.{NodeAbstract, IdTableAbstract}

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 21:28
 */

/**
 * Элемент дерева
 */
case class Node(name: String, left: Node, right: Node) extends NodeAbstract(name)

/**
 * Организация таблиц идентификаторов
 * Метод: Бинарное дерево
 */
class BinaryTree extends IdTableAbstract {

  /** Инициализация таблицы идентификаторов */
  override def init(): Unit = ???

  /** Добавление элемента в таблицу */
  override def add(idName: String): Option[NodeAbstract] = ???

  /** Поиск элемента в таблице по имени */
  override def find(idName: String): Option[NodeAbstract] = ???

  /** Очистка таблицы */
  override def clear() = ???

  /** Возврат таблицы идентификаторов */
  override def getIdTable: Seq[String] = ???

}
