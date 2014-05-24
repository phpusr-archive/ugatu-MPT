package experiment.lab3

import mpt.lab.two.automat.LexAuto
import scala.collection.mutable.ListBuffer
import mpt.lab.two.lexem.LexElem
import scala.io.Source
import mpt.lab.three.{TSymbStack, SyntSymb}

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 16:01
 */

/**
 * Тестирование Синтаксического разбора
 */
object TestSynt extends App {

  val lines = Source.fromFile("data/TestSynt.txt").getLines().toArray

  val auto = new LexAuto
  val lexList = ListBuffer[LexElem]()
  val res = auto.makeLexList(lines, lexList)
  println("lex result: " + res)

  val stack = new TSymbStack
  val root = SyntSymb.buildSyntList(lexList.toList, stack)
  println("synt result: " + root)
}
