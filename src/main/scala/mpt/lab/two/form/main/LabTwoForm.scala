package mpt.lab.two.form.main

import scala.swing._
import scala.swing.TabbedPane.Page
import scala.swing.event.ButtonClicked
import scala.io.Source
import mpt.lab.two.automat.LexAuto
import mpt.lab.two.lexem.LexElem
import scala.collection.mutable.ListBuffer
import java.awt.Font
import scala.swing.Font
import java.io.File

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 15:48
 */

/**
 * Главная форма
 */
object LabTwoForm extends SimpleSwingApplication {

  /** Путь к файлу с данными */
  private val filePathTextField = new TextField {
    editable = false
    preferredSize = defaultSize()
  }
  /** Кнопка выбора файла */
  private val browseFileButton = new Button("Browse") {
    preferredSize = defaultSize()
  }
  /** Кнопка обработки программы */
  private val processingButton = new Button("Processing") {
    preferredSize = defaultSize()
  }
  /** Текстовое поле просмотра содержимого файла */
  private val fileContentTextArea = new TextArea(20, 60) {
    border = Swing.EtchedBorder
    lineWrap = true
    font = new Font("Arial", Font.PLAIN, 12)
  }

  /** Таблица лексем */
  private val lexemTable = new Table {
    model = new LexTableModel
  }
  private val lexemModel = lexemTable.model.asInstanceOf[LexTableModel]

  /** Кнопка выхода */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(200, preferredSize.height)
  }


  //////////////////////////////////////////////////////////////


  // Генерация компонентов по умолчанию
  private def defaultScrollPane = (c: Component) => new ScrollPane() {
    viewportView = c
    verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
  }
  private def defaultSize = () => new Dimension(130, 25)

  def top = new MainFrame {
    contents = new BorderPanel {
      // Центральная панель
      layout(new TabbedPane {

        // Источник (Вкладка 1)
        pages += new Page("Source", new GridBagPanel {
          border = Swing.TitledBorder(Swing.EtchedBorder, "Source data")

          val c = new Constraints
          c.insets = new Insets(5, 5, 5, 5)

          c.weightx = 1
          c.fill = GridBagPanel.Fill.Horizontal
          layout(filePathTextField) = c

          c.weightx = 0
          layout(browseFileButton) = c

          c.gridx = 1
          c.gridy = 1
          layout(processingButton) = c

          c.gridx = 0
          c.gridy = 2
          c.gridwidth = 2
          c.weighty = 1
          c.fill = GridBagPanel.Fill.Both
          layout(defaultScrollPane(fileContentTextArea)) = c
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
        //peer.setSelectedIndex(1)

      }) = BorderPanel.Position.Center

      // Нижняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)

        c.weightx = 1
        c.anchor = GridBagPanel.Anchor.East
        layout(exitButton) = c
      }) = BorderPanel.Position.South
    }

    centerOnScreen()
  }

  // Обработчики событий формы
  listenTo(browseFileButton, processingButton)
  listenTo(exitButton)

  /** Диалог выбора файла */
  private val fileChooser = new FileChooser(new File("data"))

  reactions += {
    // Выбор файла
    case ButtonClicked(`browseFileButton`) =>
      val result = fileChooser.showDialog(null, "Select file")
      if (result == FileChooser.Result.Approve) {
        fileContentTextArea.text = Source.fromFile(fileChooser.selectedFile).mkString
        filePathTextField.text = fileChooser.selectedFile.getAbsolutePath
      }

    // Обработка текста программы
    case ButtonClicked(`processingButton`) => processing()

    // Выход из программы
    case ButtonClicked(`exitButton`) => System.exit(0)
  }

  /** Обработка текста программы */
  private def processing() {
    val lines = fileContentTextArea.text.split("\n")

    val auto = new LexAuto
    val out = ListBuffer[LexElem]()
    try {
      auto.makeLexList(lines, out)
    } catch {
      case e: Exception => println(e.getMessage)
    }

    //TODO номера и очистка
    out.map(e => Seq("0", e.lexInfo.name, e.value)).foreach(lexemModel.addRow)

  }

}
