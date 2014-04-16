package mpt.lab.one.stat

/**
 * @author phpusr
 *         Date: 16.04.14
 *         Time: 19:00
 */

/**
 * Подсчет статистики получения элементов
 */
class Stat {
  /** Кол-во элементов */
  var elementsCount = 0
  /** Число выполненных операций для текущего элемента  */
  private var currentElementCounter = 0
  /** Число выполненных операций для всех элементов  */
  private var allElementsCounter = 0

  /**
   * Новый элемент
   * Сбросить подсчет текущего
   * Пересчитать среднее
   */
  def newElement() {
    elementsCount += 1
    currentElementCounter = 0
  }
  /** Увеличить счетчик для текущего элемента */
  def inc() {
    currentElementCounter += 1
    allElementsCounter += 1
  }
  /** Среднее число операций для элемента */
  def avg() = allElementsCounter.toDouble / elementsCount
}

