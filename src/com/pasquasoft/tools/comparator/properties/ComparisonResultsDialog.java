package com.pasquasoft.tools.comparator.properties;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;

public class ComparisonResultsDialog extends JDialog implements ActionListener
{
  /**
   * Generated serial version UID.
   */
  private static final long serialVersionUID = -8452833936583897169L;

  private JPanel north = new JPanel();
  private JPanel south = new JPanel();

  private JButton save = new JButton("Save...");
  private JButton close = new JButton("Close");

  private JCheckBox differencesOnly = new JCheckBox("Only show differences");

  private PropertiesTable table;

  public ComparisonResultsDialog(Frame owner, Vector<String> columnNames,
      Vector<Vector<String>> data)
  {
    super(owner, "Comparison Results", true);

    save.addActionListener(this);
    close.addActionListener(this);
    differencesOnly.addActionListener(this);

    north.setLayout(new FlowLayout(FlowLayout.LEFT));
    north.add(differencesOnly);

    south.add(save);
    south.add(close);

    /* Components should be added to the container's content pane */
    Container cp = getContentPane();

    /* Create the table */
    table = new PropertiesTable(new PropertiesTableModel(data, columnNames));
    table.setPreferredScrollableViewportSize(new Dimension(500, 100));

    cp.add(BorderLayout.NORTH, north);
    cp.add(BorderLayout.CENTER, new JScrollPane(table));
    cp.add(BorderLayout.SOUTH, south);

    /* Add the window listener */
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt)
      {
        dispose();
      }
    });

    /* Set all buttons to the same size as the largest button */
    Dimension dimension = save.getPreferredSize();
    close.setPreferredSize(dimension);

    /* Size the dialog */
    pack();

    /* Allow resize */
    setResizable(true);

    /* Center the dialog */
    setLocationRelativeTo(owner);

    /* Show the dialog */
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent evt)
  {
    Object obj = evt.getSource();

    if (obj == save)
    {
      try
      {
        saveResults();
      }
      catch (Throwable th)
      {
        JOptionPane.showMessageDialog(ComparisonResultsDialog.this,
            th.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    else if (obj == close)
    {
      dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    else if (obj == differencesOnly)
    {
      if (differencesOnly.isSelected())
      {
        applyDifferencesOnlyFilter();
      }
      else
      {
        removeFilter();
      }
    }
  }

  /**
   * Applies a row filter that only displays table rows whose properties values
   * are different between the properties files.
   */
  private void applyDifferencesOnlyFilter()
  {
    RowFilter<PropertiesTableModel, Object> rowFilter = new RowFilter<PropertiesTableModel, Object>() {
      public boolean include(
          Entry<? extends PropertiesTableModel, ? extends Object> entry)
      {
        int row = (Integer) entry.getIdentifier();
        row = table.convertRowIndexToView(row);

        String value1 = (String) table.getValueAt(row, 1);
        String value2 = (String) table.getValueAt(row, 2);

        return !value1.equalsIgnoreCase(value2);
      }
    };

    table.getCustomSorter().setRowFilter(rowFilter);
  }

  /**
   * Removes the applied row filter.
   */
  private void removeFilter()
  {
    table.getCustomSorter().setRowFilter(null);
  }

  /**
   * Saves the results of the properties files comparison to a file.
   * 
   * @throws FileNotFoundException if an error occurs while opening or creating
   *         the file
   */
  private void saveResults() throws FileNotFoundException
  {
    PrintWriter pw = null;
    JFileChooser fc = new JFileChooser();

    int choice = fc.showSaveDialog(ComparisonResultsDialog.this);

    if (choice == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        pw = new PrintWriter(fc.getSelectedFile());

        int rows = table.getRowCount();
        int columns = table.getColumnCount();

        for (int row = 0; row < rows; row++)
        {
          for (int column = 0; column < columns; column++)
          {
            pw.print(table.getValueAt(row, column) + ",");
          }

          String value1 = (String) table.getValueAt(row, 1);
          String value2 = (String) table.getValueAt(row, 2);

          pw.println(value1.equalsIgnoreCase(value2) ? "same" : "different");
        }
      }
      finally
      {
        if (pw != null)
        {
          pw.close();
        }
      }
    }
  }
}
