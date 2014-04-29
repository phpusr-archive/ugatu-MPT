package mpt.lab.two.automat

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 15:50
 */

/**
 * Возможные состояния автомата
 */
object AutoPos extends Enumeration {

  type AutoPos = Value

  /** Начальное состояние */
  val H = Value

  /** Комментарий */
  val C = Value

  /** Двоеточие */
  val G = Value

  /** Идетификатор */
  val V = Value

  /** Числовая константа */
  val D = Value

  /** Конченое состояние */
  val F = Value

}
