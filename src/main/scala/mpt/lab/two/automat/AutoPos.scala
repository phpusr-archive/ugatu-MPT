package mpt.lab.two.automat

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:50
 */

/**
 * Возможные состояния автомата
 */
object AutoPos {

  case class AutoPos(name: String, description: String)

  /** Начальное состояние */
  val H = AutoPos("H", "Начальное состояние")

  /** Комментарий */
  val C = AutoPos("C", "Комментарий")

  /** Двоеточие */
  val G = AutoPos("G", "Двоеточие")

  /** Идетификатор */
  val V = AutoPos("V", "Идетификатор")

  /** Числовая константа */
  val D = AutoPos("D", "Числовая константа")

  /** Конченое состояние */
  val F = AutoPos("F", "Конченое состояние")

}
