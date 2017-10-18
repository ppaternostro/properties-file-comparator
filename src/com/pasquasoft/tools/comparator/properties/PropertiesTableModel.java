package com.pasquasoft.tools.comparator.properties;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * A custom model whose cells are non-editable.
 * 
 * @version 1.0
 */
public class PropertiesTableModel extends DefaultTableModel
{
  private static final long serialVersionUID = -4004973518191120148L;

  /**
   * Constructs a table model with the specified parameters.
   * 
   * @param data the data
   * @param columnNames the column names
   */
  public PropertiesTableModel(Vector<?> data, Vector<?> columnNames)
  {
    super(data, columnNames);
  }

  @Override
  public boolean isCellEditable(int row, int column)
  {
    return false;
  }
}
