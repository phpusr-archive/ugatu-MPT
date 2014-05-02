package experiment.lab2

import mpt.lab.two.automat.LexAuto
import scala.collection.mutable.ListBuffer
import mpt.lab.two.lexem.LexElem
import scala.io.Source

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 16:01
 */

/**
 * Тестирование конечного автомата
 */
object TestAutomat extends App {

  val lines = Source.fromFile("data/TestProg.txt").getLines().toArray

  val auto = new LexAuto
  val out = ListBuffer[LexElem]()
  auto.makeLexList(lines, out)

}
