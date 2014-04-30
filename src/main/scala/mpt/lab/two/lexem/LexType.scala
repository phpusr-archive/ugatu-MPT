package mpt.lab.two.lexem

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 14:31
 */

case class LexType(name: String, info: String)

/**
 * Типы лексем
 *
 * 3. Входной  язык  содержит  операторы  условия  типа  if ...  then   ...  else  и  if ...  then ,
 * разделенные  символом  ;  (точка  с  запятой).  Операторы  условия  содержат  идентификаторы,
 * знаки сравнения <, >, =, десятичные числа с плавающей точкой  (в
 * обычной и логарифмической форме), знак присваивания (:=).
 */
object LexType {
  private val KeyWord = "Ключевое слово"
  val Var = "Переменная"
  private val Const = "Константа"
  private val SignAssignment = "Знак присвоения"
  val MeaninglessSymbol = "Незначащий символ"

  // TODO continue
  val If = LexType(KeyWord, "if")

  /** Конец строки */
  val LexStart = LexType(MeaninglessSymbol, "\n")
}
