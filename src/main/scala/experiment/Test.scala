package experiment

import mpt.lab.one.idtable.rehash.RehashTable
import mpt.lab.one.idtable.binarytree.BinaryTree

/**
 * Тестирование работы таблиц ид-ов
 */
object Test extends App {

  val list = List("abc", "grh", "bmw", "kldk", "grh")

  /** Таблица с рехэшированием */
  def testRehash() {

    val table = new RehashTable(3)
    list.foreach(table.add)
    val res = table.getIdTable

    println("res: " + res.mkString(", "))

    println("find bmw: " + table.find("bmw"))
    println("find bmw2: " + table.find("bmw2"))
    println("stat: " + table.getStat)
  }

  /** Бинарное дерево */
  def testBinaryTree() {
    val table = new BinaryTree(20)
    list.foreach(table.add)

    val res = table.getIdTable

    println("res: " + res.mkString(", "))

    println("find bmw: " + table.find("bmw"))
    println("find bmw2: " + table.find("bmw2"))
    println("stat: " + table.getStat)
  }

  testBinaryTree()

}