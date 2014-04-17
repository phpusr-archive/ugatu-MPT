package mpt.lab.one.run

import javax.swing.UIManager
import mpt.lab.one.form.main.MainForm


/**
 * @author phpusr
 *         Date: 03.04.14
 *         Time: 15:33
 */

/**
 * Запуск программы
 */
object Main extends App {
  // Установка темы оформления
  //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")
  UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName)
  // Создание и запуск главной формы
  MainForm.main(args)
}
