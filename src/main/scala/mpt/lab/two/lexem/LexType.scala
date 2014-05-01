package mpt.lab.two.lexem

/**
 * @author phpusr
 *         Date: 29.04.14
 *         Time: 14:31
 */

case class LexType(name: String, info: Option[String])

/**
 * Типы лексем
 *
 * 3. Входной  язык  содержит  операторы  условия  типа  if ...  then   ...  else  и  if ...  then ,
 * разделенные  символом  ;  (точка  с  запятой).  Операторы  условия  содержат  идентификаторы,
 * знаки сравнения <, >, =, десятичные числа с плавающей точкой  (в
 * обычной и логарифмической форме), знак присваивания (:=).
 */
object LexType {
  val KeyWord = "Ключевое слово"
  val Var = "Переменная"
  val Const = "Константа"
  val AssignmentSign = "Знак присвоения"
  val SplitterSign = "Символ-разделитель"
}

/**
 * Ключевые слова
 *
 * TODO уменьшить доступ
 */
object LexKeyWord {
  import mpt.lab.two.lexem.LexType._

  val If = "if"
  val Then = "then"
  val Else = "else"

  val Words = Array(If, Then, Else)

  /** Возвращает ключевое слово по имени */
  def getKeyWordByName(name: String) = {
    if (Words.contains(name)) {
      Some(createKeyWord(name))
    } else {
      None
    }
  }

  /** Создание ключевого слова по имени */
  private def createKeyWord(info: String) = LexType(KeyWord, Some(info))

  //////////////////////////////////////////

  /** Знак присвоения */
  val Assignment = ":="

  /** Возвращает оператор по имени */
  def getOperatorByName(name: String) = {
    name match {
      case Assignment => LexType(AssignmentSign, Some(Assignment))
    }
  }

  //////////////////////////////////////////

  /** Конец строки */ //TODO убрать или использовать
  val LexStart = LexType(SplitterSign, Some("\n"))

}
