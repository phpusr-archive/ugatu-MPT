package mpt.lab.three.syntax

import mpt.lab.two.lexem.LexType
import mpt.lab.three.symbol.TSymbStack
import mpt.lab.three.syntax.Types.TLexem

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

  /** Состовляет основу */
  val Basis = '='
  /** Предшуствует */
  val Preceded = '<'
  /** Следует */
  val Follow = '>'
  /** Не сопоставимы */
  val Empty = ' '

  /** Матрица операторного предшествования */
  val GrammMatrix: Array[Array[Char]] = Array(
    Array(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '>'), // ;
    Array(' ', ' ', '=', ' ', '<', ' ', '<', '<', '<', '<', ' ', ' '), // if
    Array('>', '<', ' ', '=', '<', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // then
    Array('>', '<', ' ', '>', '<', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // else
    Array('>', ' ', '>', '>', ' ', '=', '>', '>', '>', ' ', '>', ' '), // a
    Array('>', ' ', '>', '<', ' ', ' ', '<', '<', '<', '<', ' ', ' '), // :=
    Array('>', ' ', '>', '>', '<', ' ', '>', '>', '<', '<', '>', ' '), // <
    Array('>', ' ', '>', '>', '<', ' ', '>', '>', '<', '<', '>', ' '), // >
    Array('>', ' ', '>', '>', '<', ' ', '>', '>', '>', '<', '>', ' '), // ==
    Array(' ', ' ', ' ', ' ', '<', ' ', '<', '<', '<', '<', '=', ' '), // (
    Array('>', ' ', '>', '>', ' ', ' ', '>', '>', '>', ' ', '>', ' '), // )
    Array('<', '<', ' ', ' ', '<', ' ', ' ', ' ', ' ', ' ', ' ', ' ')  // Начало
  )

  val E = "E"

  /** Правила грамматики */
  val GrammRules: Array[Array[String]] = Array(
    Array(E, ";"), // 1
    Array("if", E, "then", E, "else", E), // 2
    Array("if", E, "then", E), // 3
    Array("a", ":=", E), // 4
    Array("if", E, "then", E, "else", E), // 5
    Array("a", ":=", E), // 6
    Array(E, "<", E), // 7
    Array(E, ">", E), // 8
    Array(E), // 9
    Array(E, "==", E), // 10
    Array(E), // 11
    Array("a"), // 12
    Array("(", E, ")") // 13
  )

  /** Корректировка отношения */
  def correctRule(cRule: Char, lexTCur: TLexem, lex: LexType, symbStack: TSymbStack): Char = cRule

  /** Наименование нетерминального символа в правилах остновной грамматики */
  def makeSymbolStr(ruleNum: Int): String = E

}
