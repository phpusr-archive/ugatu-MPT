package mpt.lab.two.automat

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:50
 */

/**
 * Состояние автомата
 */
case class AutoPos(name: String, description: String)

/**
 * Возможные состояния автомата
 */
object AutoPos {

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

  /** Вещественное число */
  val P = AutoPos("P", "Вещественное число")

  /** Конченое состояние */
  val F = AutoPos("F", "Конченое состояние")

}
