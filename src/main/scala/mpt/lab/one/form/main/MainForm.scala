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
  def top: Frame = new MainFrame {
    size = new Dimension(600, 300)
    centerOnScreen()
  }
}
