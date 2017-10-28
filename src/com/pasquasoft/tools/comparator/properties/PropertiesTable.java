package com.pasquasoft.tools.comparator.properties;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 * A custom table that sets row color based on column data.
 * 
 * @version 1.0
 */
public class PropertiesTable extends JTable
{
  /**
   * Generated serial version UID.
   */
  private static final long serialVersionUID = 3496544924798345161L;

  private TableRowSorter<PropertiesTableModel> sorter;

  /**
   * Constructs a table with the specified table model.
   * 
   * @param tm the table model
   */
  public PropertiesTable(PropertiesTableModel ptm)
  {
    super(ptm);

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    /* Create custom sorter so we can apply a filter */
    sorter = new TableRowSorter<PropertiesTableModel>(ptm);
    setRowSorter(sorter);

    /* Sort the key column in ascending order */
    List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
    sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
    getRowSorter().setSortKeys(sortKeys);
  }

  @Override
  public Component prepareRenderer(TableCellRenderer renderer, int row,
      int column)
  {
    Component component = super.prepareRenderer(renderer, row, column);

    if (!isRowSelected(row))
    {
      component.setBackground(getBackground());
      int modelRow = convertRowIndexToModel(row);
      String value1 = (String) getModel().getValueAt(modelRow, 1);
      String value2 = (String) getModel().getValueAt(modelRow, 2);
      component.setBackground(value1.equalsIgnoreCase(value2) ? Color.GREEN
          : Color.RED);
    }

    return component;
  }

  /**
   * Retrieves the custom sorter.
   * 
   * @return the custom sorter
   */
  public TableRowSorter<PropertiesTableModel> getCustomSorter()
  {
    return sorter;
  }
}
