package mpt.lab.one.form.main

import scala.swing._

/**
 * @author phpusr
 *         Date: 17.04.14
 *         Time: 11:56
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication {

  /** Заголовок для панели (по умолчанию) */
  private def defaultTitleBorder = (title: String) => Swing.TitledBorder(Swing.EtchedBorder, title)

  // ФормаMainForm
  def top: Frame = new MainFrame {
    contents = new GridBagPanel {
      import GridBagPanel.Fill._

      // Левая часть
      val c = new Constraints
      c.weightx = 0.5
      c.weighty = 1
      c.fill = Both
      layout(new BorderPanel {
        border = defaultTitleBorder("Input data")
        layout(new TextArea {
          preferredSize = new Dimension(300, 0)
        }) = BorderPanel.Position.Center
      }) = c

      // Правая часть
      layout(new GridBagPanel {
        // Панель исходных данных
        val c = new Constraints
        c.weightx = 1
        c.fill = Both
        layout(new FlowPanel {
          border = defaultTitleBorder("Source")
          contents += new Button("Generate")
        }) = c

        // Панель поиска идентификатора
        c.gridy = 1
        layout(new GridBagPanel {
          // Поле вводе элемента для поиска
          border = defaultTitleBorder("Find ids")
          val c = new Constraints
          c.fill = Horizontal
          c.weightx = 1
          layout(new TextField()) = c

          // Кнопка поиска
          c.fill = None
          c.weightx = 0
          layout(new Button("Find")) = c

          // Панель с выводом кол-ва и кнопкой сброса
          c.gridy = 1
          c.anchor = GridBagPanel.Anchor.West
          layout(new FlowPanel {
            contents += new Label("All find: ")
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
            layout(new BoxPanel(Orientation.Vertical) {
              border = defaultTitleBorder("Rehash")
            }) = c

            // Правая панель
            layout(new BoxPanel(Orientation.Vertical) {
              border = defaultTitleBorder("Binary tree")
            }) = c
          }) = c

          // Кнопка выхода
          c.gridy = 4
          c.fill = None
          c.anchor = GridBagPanel.Anchor.Center
          layout(new Button("Exit") {
            preferredSize = new Dimension(200, preferredSize.height)
          }) = c

        }) = c
      }) = c
    }

    //size = new Dimension(600, 300)
    centerOnScreen()
  }
}
