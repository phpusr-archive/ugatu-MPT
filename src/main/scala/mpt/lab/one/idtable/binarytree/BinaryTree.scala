package mpt.lab.one.idtable.binarytree

import mpt.lab.one.idtable.{NodeAbstract, IdTableAbstract}
import mpt.lab.one.idtable.IndexType._
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 21:28
 */

/**
 * Элемент дерева
 */
case class Node(name: String, hash: Index, var left: Option[Node], var right: Option[Node]) extends NodeAbstract(name, hash) {
  def this(name: String, hash: Index) = this(name, hash, None, None) //TODO
}

/**
 * Организация таблиц идентификаторов
 * Метод: Бинарное дерево
 */
class BinaryTree(MaxTableSize: Index) extends IdTableAbstract(MaxTableSize) {

  /** Корневой элемент дерева */
  private var root: Option[Node] = None

  /** Инициализация таблицы идентификаторов */
  override def init(): Unit = ???

  /** Добавление элемента в таблицу */
  override def add(idName: String): Option[Node] = {
    val hash = getHash(idName)
    if (root.isEmpty) {
      root = Some(Node(idName, hash, None, None))
      root
    } else {
      addRec(idName, hash, root)
    }
  }

  /** Рекурсивное добавление элемента в дерево */
  private def addRec(idName: String, hash: Index, node: Option[Node]): Option[Node] = {
    if (node.isEmpty) { // Если узел пустой
      new Some(Node(idName, hash, None, None)) //Создаем новый узел, знач-е узла берем из idName
    } else if (hash == node.get.hash) { // Если hash == текущему узлу
      println(">> Already exists!") //TODO
      None
    } else if (hash > node.get.hash) { // Если hash > текущего узла
      node.get.right = addRec(idName, hash, node.get.right)
      node
    } else if (hash < node.get.hash) { // Если hash < текущего узла
      node.get.left = addRec(idName, hash, node.get.left)
      node
    } else {
      assert(assertion = false, ">> The condition is not provided") //TODO
      None
    }
  }

  /** Поиск элемента в таблице по имени */
  override def find(idName: String): Option[Node] = ???

  /** Очистка таблицы */
  override def clear() = ???

  /** Возврат таблицы идентификаторов */
  override def getIdTable: Seq[String] = {
    val list = ListBuffer[String]()
    buildIdTableRec(root, list)
    
    list
  }

  /** Рекурсивный возврат таблицы идентификаторов */
  private def buildIdTableRec(node: Option[Node], list: ListBuffer[String]) {
    if (!node.isEmpty) {
      buildIdTableRec(node.get.left, list)
      list += node.get.name
      buildIdTableRec(node.get.right, list)
    }
  }

}
