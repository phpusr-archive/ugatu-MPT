package mpt.lab.two.form.main

import scala.swing._
import scala.swing.TabbedPane.Page
import scala.swing.event.ButtonClicked

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 15:48
 */

/**
 * Главная форма
 */
object LabTwoForm extends SimpleSwingApplication {

  /** Кнопка выхода */
  val exitButton = new Button("Exit") {
    preferredSize = new Dimension(200, preferredSize.height)
  }

  /** Таблица лексем */
  val lexemTable = new Table {
    model = new LexTableModel
  }
  val lexemModel = lexemTable.model.asInstanceOf[LexTableModel]

  def top = new MainFrame {
    contents = new BorderPanel {
      // Центральная панель
      layout(new TabbedPane {

        // Источник (Вкладка 1)
        pages += new Page("Source", new GridBagPanel {
          val c = new Constraints
          layout(new Button("Test")) = c
        })

        // Таблица лексем (Вкладка 2)
        pages += new Page("Lexem table", new GridBagPanel {
          val c = new Constraints
          c.weightx = 1
          c.weighty = 1
          c.fill = GridBagPanel.Fill.Both
          layout(new ScrollPane(lexemTable)) = c
        })

        // Вкладка по умолчанию
        peer.setSelectedIndex(1)

      }) = BorderPanel.Position.Center

      // Нижняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.weightx = 1
        c.anchor = GridBagPanel.Anchor.West
        layout(exitButton) = c
      }) = BorderPanel.Position.South
    }

    size = new Dimension(800, 600) //TODO потом убрать

    centerOnScreen()
  }

  // Обработчики событий формы
  listenTo(exitButton)

  reactions += {

    // Выход из программы
    case ButtonClicked(`exitButton`) => System.exit(0)
  }

  lexemModel.addRow(List("One", "Two", "3"))

}
