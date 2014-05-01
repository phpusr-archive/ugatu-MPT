package mpt.lab.two.form.main

import javax.swing.table.AbstractTableModel
import scala.collection.mutable.ArrayBuffer

/**
 * @author phpusr
 *         Date: 01.05.14
 *         Time: 16:52
 */

/**
 * Модель таблицы лексем
 */
class LexTableModel extends AbstractTableModel {

  /** Заголовки таблицы */
  private val columnNames = Array("№", "Lexem", "Value")

  /** Данные таблицы */
  private val data = new ArrayBuffer[Seq[String]]

  def addRow(rowData: Seq[String]) {
    data += rowData
    fireTableRowsInserted(data.size - 1, data.size - 1)
  }

  override def getColumnCount = columnNames.size

  override def getRowCount = data.size

  override def getColumnName(col: Int) = columnNames(col)

  override def getValueAt(row: Int, col: Int) = data(row)(col)

  override def isCellEditable(row: Int, col: Int) = false

  override def getColumnClass(c: Int) = getValueAt(0, c).getClass
  
};
