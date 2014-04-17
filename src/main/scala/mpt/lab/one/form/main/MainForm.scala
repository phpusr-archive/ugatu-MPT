package mpt.lab.one.form.main

import scala.swing._
import scala.swing.event.ButtonClicked
import scala.util.Random

/**
 * @author phpusr
 *         Date: 17.04.14
 *         Time: 11:56
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication {

  /** Кол-во сгенерированных ид-ов */
  private val IdsCount = 10
  /** Кол-во символов в ид-е */
  private val IdSymbolsCount = 32

  /** Кнопка генерации идентификаторов */
  private val generateIdsButton = new Button("Generate")
  /** Текстовая область ввода ид-ов */
  private val idsTextArea = new TextArea {
    preferredSize = new Dimension(300, 0)
    lineWrap = true
  }
  /** Кнопка очистки области ввода ид-ов */
  private val clearIdsButton = new Button("Clear") {
    preferredSize = new Dimension(130, preferredSize.height)
  }

  /** Кнопка выхода */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, preferredSize.height)
  }
  
  private val leftStatPanel = defaultStatPanel()
  private val rightStatPanel = defaultStatPanel()


  //--------------------------------

  /** Заголовок для панели (по умолчанию) */
  private def defaultTitleBorder = (title: String) => Swing.TitledBorder(Swing.EtchedBorder, title)
  /** Панель со значением (по умолчанию) */
  private def defaultValuePanel = (title: String, valueLabel: Label) => new FlowPanel {
    contents += new Label(title)
    contents += valueLabel
  }
  /** Панель статистики (по умолчанию) */
  private def defaultStatPanel = () => {
    new GridBagPanel {
      border = defaultTitleBorder("Rehash")
      val c = new Constraints
      c.weightx = 1
      c.anchor = GridBagPanel.Anchor.Center
      layout(new Label("Id found")) = c
      c.anchor = GridBagPanel.Anchor.West
      c.gridy = 1
      layout(defaultValuePanel("Equals", new Label("0"))) = c
      c.gridy = 2
      layout(defaultValuePanel("All equals", new Label("0"))) = c
      c.gridy = 3
      layout(defaultValuePanel("Avg equals", new Label("0"))) = c
    }
  }

  // ФормаMainForm
  def top: Frame = new MainFrame {
    contents = new GridBagPanel {
      import GridBagPanel.Fill._

      // Левая часть
      val c = new Constraints
      c.weightx = 0.5
      c.weighty = 1
      c.fill = Both
      layout(new GridBagPanel {
        border = defaultTitleBorder("Input data")
        val c = new Constraints
        c.weightx = 1
        c.weighty = 1
        c.fill = Both
        layout(idsTextArea) = c
        c.gridy = 1
        c.weighty = 0
        c.fill = None
        layout(clearIdsButton) = c
      }) = c

      // Правая часть
      layout(new GridBagPanel {
        // Панель исходных данных
        val c = new Constraints
        c.weightx = 1
        c.fill = Both
        layout(new FlowPanel {
          border = defaultTitleBorder("Source")
          contents += generateIdsButton
        }) = c

        // Панель поиска идентификатора
        c.gridy = 1
        layout(new GridBagPanel {
          // Поле ввода элемента для поиска
          border = defaultTitleBorder("Find ids")
          val c = new Constraints
          c.fill = Horizontal
          c.weightx = 1
          layout(new TextField() {
            preferredSize = new Dimension(200, preferredSize.height)
          }) = c

          // Кнопка поиска
          c.fill = None
          c.weightx = 0
          layout(new Button("Find")) = c

          // Панель с выводом кол-ва и кнопкой сброса
          c.gridy = 1
          c.anchor = GridBagPanel.Anchor.West
          layout(new FlowPanel {
            contents += new Label("All found: ")
            contents += new Label("161")
          }) = c
          layout(new Button("Reset")) = c

          // Кнопка найти все
          c.gridy = 2
          layout(new Button("Find all")) = c

          // Панель с выводом статистики
          c.gridy = 3
          c.gridwidth = 2
          c.fill = Both
          layout(new GridBagPanel {
            // Левая панель
            val c = new Constraints
            c.weightx = 0.5
            c.fill = Both
            layout(leftStatPanel) = c

            // Правая панель
            layout(rightStatPanel) = c
          }) = c

        }) = c // end search panel


        // Кнопка выхода
        c.gridy = 2
        c.fill = None
        c.anchor = GridBagPanel.Anchor.Center
        layout(exitButton) = c

      }) = c // end right part
    } // end main panel

    //size = new Dimension(600, 300)
    centerOnScreen()
  }


  // Обработчики событий формы
  listenTo(generateIdsButton, clearIdsButton)
  listenTo(exitButton)

  reactions += {
    // Генерация ид-ов
    case ButtonClicked(`generateIdsButton`) => idsTextArea.append(generateIds())

    // Очистка поле ввода ид-во
    case ButtonClicked(`clearIdsButton`) => idsTextArea.text = ""

    // Выход из программы
    case ButtonClicked(`exitButton`) => System.exit(0)
  }

  /** Генерация строки с ид-ми */
  private def generateIds() = {
    val str = new StringBuilder
    for (i <- 1 to IdsCount) {
      str.append(Random.nextString(IdSymbolsCount) + "\n")
    }
    str.toString()
  }

}
