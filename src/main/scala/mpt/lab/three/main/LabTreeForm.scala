package mpt.lab.three.main

import scala.swing._
import scala.swing.TabbedPane.Page
import scala.swing.event.ButtonClicked
import scala.io.Source
import mpt.lab.two.automat.LexAuto
import mpt.lab.two.lexem.{Position, LexControl, LexElem}
import scala.collection.mutable.ListBuffer
import java.awt.Font
import scala.swing.Font
import java.io.File
import javax.swing.JTabbedPane
import mpt.lab.two.form.main.LexTableModel
import scalaswingcontrib.tree.{TreeModel, Tree}
import mpt.lab.three.syntax.SyntSymb
import mpt.lab.three.symbol.{TSymbKind, TSymbStack, TSymbol}

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 15:48
 */

/**
 * Главная форма
 */
object LabTreeForm extends SimpleSwingApplication {

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
    peer.getColumnModel.getColumn(0).setPreferredWidth(10)
    peer.getColumnModel.getColumn(2).setPreferredWidth(300)
  }
  private val lexemModel = lexemTable.model.asInstanceOf[LexTableModel]

  /** Лейбл статуса лексического разбора */
  private val lexParsingStatusLabel = new Label
  /** Лейбл статуса лексического разбора */
  private val syntaxParsingStatusLabel = new Label("Test")
  /** Кнопка выхода */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(200, preferredSize.height)
  }

  /** Компонент с вкладками */
  private var tabbedPane: JTabbedPane = null

  /** Синтаксическое дерево */
  val syntaxTree = new Tree[TSymbol] {
    renderer = Tree.Renderer {e =>
      // Если нетерминальный символ, отобразить помимо символа № правила свертки
      if (e.symbType == TSymbKind.SymbSynt) s"${e.symbolStr} (${e.ruleNum})"
      else e.symbolStr
    }
  }

  //////////////////////////////////////////////////////////////

  // !!! Тестирование
  private val test = new Thread(new Runnable {
    override def run() {
      fileContentTextArea.text = Source.fromFile("data/TestSynt.txt").mkString
      Thread.sleep(1000)
      processing()
    }
  })
  if (true) test.start()

  //////////////////////////////////////////////////////////////

  // Генерация компонентов по умолчанию
  private def defaultScrollPane = (c: Component) => new ScrollPane() {
    viewportView = c
    verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
  }
  private def defaultSize = () => new Dimension(130, 25)

  def top = new MainFrame {
    contents = new BorderPanel {
      title = "Syntax tree builder (Doronin S. PRO-301v)"

      // Центральная панель
      layout(new TabbedPane {
        tabbedPane = peer

        // Источник (Вкладка 1)
        val NameTabSource = "Source"
        pages += new Page(NameTabSource, new GridBagPanel {
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
        }, NameTabSource)

        // Таблица лексем (Вкладка 2)
        val NameTabLexem = "Lexem table"
        pages += new Page(NameTabLexem, new GridBagPanel {
          val c = new Constraints
          c.weightx = 1
          c.weighty = 1
          c.fill = GridBagPanel.Fill.Both
          layout(new ScrollPane(lexemTable)) = c
        }, NameTabLexem)

        // Синтаксическое дерево (Вкладка 3)
        val NameTabSyntaxTree = "Syntax tree"
        pages += new Page(NameTabSyntaxTree, new GridBagPanel {
          val c = new Constraints
          c.weightx = 1
          c.weighty = 1
          c.fill = GridBagPanel.Fill.Both
          layout(defaultScrollPane(syntaxTree)) = c
        }, NameTabSyntaxTree)

      }) = BorderPanel.Position.Center

      // Нижняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)

        c.weightx = 1
        layout(lexParsingStatusLabel) = c

        c.gridy = 1
        layout(syntaxParsingStatusLabel) = c

        c.gridy = 0
        c.gridheight = 2
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
    // Очистка таблицы
    lexemModel.clear()
    val root = TSymbol.createLex(LexElem.createKey("", Position(0, 0, 0)))
    syntaxTree.model = TreeModel(root)(_.children)

    // Разбор текста
    val auto = new LexAuto
    val lines = fileContentTextArea.text.split("\n")
    val lexList = ListBuffer[LexElem]()
    val statusOrPos = auto.makeLexList(lines, lexList)

    // Установка статуса разбора текста
    if (statusOrPos == LexAuto.NoErrors) {
      lexParsingStatusLabel.text = "Лексический разбор выполнен без ошибок"

      // Добавление конечной лексемы
      val lexListForSyntax = lexList.toList :+ LexControl.LexStop

      val symbStack = new TSymbStack
      val rootSymbol = SyntSymb.buildSyntList(lexListForSyntax, symbStack)
      syntaxTree.model = TreeModel(rootSymbol)(_.children)
      syntaxTree.expandAll()
      
      if (rootSymbol.lexem == null) {
        syntaxParsingStatusLabel.text = "Синтаксический разбор выполнен без ошибок"
      } else {
        syntaxParsingStatusLabel.text = "В ходе синтаксического разбора обнаружена ошибка"
      }

      // Переключение на вкладку с синтаксическим деревом
      tabbedPane.setSelectedIndex(2)
    } else {
      lexParsingStatusLabel.text = "В ходе лексического разбора обнаружена ошибка"
      syntaxParsingStatusLabel.text = ""

      // Переключение на вкладку с таблицей лексем
      tabbedPane.setSelectedIndex(1)
    }

    // Добавление лексем в таблицу
    lexList.zipWithIndex.map {
      case (e, index) => Seq(s"${index+1}", e.lexInfo.name, e.value)
    }.foreach(lexemModel.addRow)

  }

}
