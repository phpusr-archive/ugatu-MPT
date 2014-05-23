package mpt.lab.three

import mpt.lab.two.lexem.LexType
import mpt.lab.three.Types.TLexem

/**
 * @author phpusr
 *         Date: 22.05.14
 *         Time: 16:07
 */

/**
 * Модуль описания матрицы предшествования и
 * правил грамматики
 */
object SyntRule {

  val Equals = "="

  val GrammMatrix: Array[Array[Char]] = ???

  val GrammRules: Array[String] = ???

  /** Корректировка отношения */
  def correctRule(cRule: Char, lexTCur: TLexem, lex: LexType, symbStack: TSymbStack): Char = cRule

}
