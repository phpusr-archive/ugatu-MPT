package experiment.lab3

import scala.swing.{FlowPanel, MainFrame, SimpleSwingApplication}
import scalaswingcontrib.tree.{TreeModel, Tree}

/**
 * @author phpusr
 *         Date: 24.05.14
 *         Time: 18:32
 */

/**
 * Тестирование работы с TreeView
 */
object TestTreeView extends SimpleSwingApplication {

  case class Node[A](value: A, children: Node[A]*)
  val menuItems = Node("Books", Node("Kiiosaki", Node("Robert")), Node("Hill"))

  val tree = new Tree[Node[String]] {
    model = TreeModel(menuItems)(_.children)
    renderer = Tree.Renderer(_.value)
  }
  tree.expandAll()

  def top = new MainFrame {
    contents = new FlowPanel {
      contents += tree
    }

    centerOnScreen()
  }

}
