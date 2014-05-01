package mpt.lab.two.automat

import scala.util.matching.Regex

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 13:31
 */

/**
 * Сверяет содержимое строки
 */
object StringMatcher {

  /** Строка состоит из переданных символов */
  def isListened = (string: String, includeChars: String) => {
    val splitterRegex = new Regex(s"^[$includeChars]+$$")

    splitterRegex.findFirstIn(string).isDefined
  }


  /** Строка состоит из незначащих символов */
  def isWhitespace = (string: String) => {
    val whitespaceRegex = new Regex("^\\s+$")

    whitespaceRegex.findFirstIn(string).isDefined
  }

  /** Строка состоит из букв */
  def isLetter = (string: String) => isLetterWithExclude(string, null)

  /** Строка состоит из букв и не включает $excludeChars */
  def isLetterWithExclude = (string: String, excludeChars: String) => {
    val letterRegex = new Regex("^[a-zA-Z_]+$")
    val isLetter = letterRegex.findFirstIn(string).isDefined

    val isNotExclude = if (excludeChars != null) {
      val excludeRegex = new Regex(s"[$excludeChars]")
      excludeRegex.findFirstIn(string).isEmpty
    } else true

    isLetter && isNotExclude
  }

  /** Строка состоит из цифр */
  def isDigit = (string: String) => {
    val digitRegex = new Regex("^\\d+$")

    digitRegex.findFirstIn(string).isDefined
  }

  /** Строка состоит из любых символов кроме $excludeChars */
  def isAnyChar = (string: String, excludeChars: String) => {
    val anyCharRegex = new Regex("^.+$")
    val excludeRegex = new Regex(s"[$excludeChars]")

    val isAnyChar = anyCharRegex.findFirstIn(string).isDefined
    val isNotExclude = excludeRegex.findFirstIn(string).isEmpty

    isAnyChar && isNotExclude
  }

}
