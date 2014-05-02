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
  val Info = "Информация"
  //TODO remove word sign
  val AssignmentSign = "Оператор присвоения"
  val SplitterSign = "Символ-разделитель"
  val EqualsSign = "Оператор сравнения"
}

/**
 * Ключевые слова
 */
object LexKeyWord {
  import mpt.lab.two.lexem.LexType._

  private val If = "if"
  private val Then = "then"
  private val Else = "else"

  private val Words = Array(If, Then, Else)

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

}

/**
 * Операторы
 */
object LexOperators {
  import mpt.lab.two.lexem.LexType._

  /** Присвоения */
  val Assignment = ":="
  // Сравнения
  val Equals = "=="
  val G = ">"
  val GE = ">="
  val L = "<"
  val LE = "<="

  /** Возвращает оператор по имени */
  def getOperatorByName(name: String) = {
    name match {
      case Assignment => LexType(AssignmentSign, Some(Assignment))
      case Equals => LexType(EqualsSign, Some(Equals))
    }
  }

}
