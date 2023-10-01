package com.pasquasoft.tools.comparator.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PropertiesFileComparatorFrame extends JFrame implements ActionListener
{
  /**
   * Generated serial version UID.
   */
  private static final long serialVersionUID = 5150276765350676534L;

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String LOOK_AND_FEEL = "laf";

  private JPanel center = new JPanel();
  private JPanel row1 = new JPanel();
  private JPanel row2 = new JPanel();
  private JPanel south = new JPanel();

  private JMenuBar mb = new JMenuBar();

  private JMenu view = new JMenu("View");
  private JMenu look = new JMenu("Look and Feel");

  private ButtonGroup looksGroup = new ButtonGroup();

  private JRadioButtonMenuItem looks[];

  private JTextField propertiesFile1 = new JTextField(30);
  private JTextField propertiesFile2 = new JTextField(30);

  private JButton compare = new JButton("Compare...");
  private JButton exit = new JButton("Exit");
  private JButton about = new JButton("About...");
  private JButton openPropertiesFile1 = new JButton();
  private JButton openPropertiesFile2 = new JButton();

  private Preferences prefs;

  public PropertiesFileComparatorFrame(String title)
  {
    super(title);

    prefs = Preferences.userNodeForPackage(PropertiesFileComparatorFrame.class);

    UIManager.LookAndFeelInfo[] installedLooks = UIManager.getInstalledLookAndFeels();

    looks = new JRadioButtonMenuItem[installedLooks.length];

    String defaultLaf = UIManager.getLookAndFeel().getName();

    defaultLaf = prefs.get(LOOK_AND_FEEL, defaultLaf);

    /*
     * Create menu items, add action listener, set the action command to the
     * Look and Feel class name, set selected menu item, and add to Look and
     * Feel menu and group.
     */
    for (int i = 0; i < looks.length; i++)
    {
      String installedLaf = installedLooks[i].getName();

      looks[i] = new JRadioButtonMenuItem(installedLaf);
      looks[i].addActionListener(this);
      looks[i].setActionCommand(installedLooks[i].getClassName());

      if (installedLaf.equals(defaultLaf))
      {
        looks[i].setSelected(true);
      }

      looksGroup.add(looks[i]);
      look.add(looks[i]);
    }

    /* Set layouts */
    setLayout(new BorderLayout());
    center.setLayout(new GridLayout(2, 1));
    row1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
    row2.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

    propertiesFile1.setEditable(false);
    propertiesFile2.setEditable(false);
    compare.setEnabled(false);

    /* Add components to panels */
    row1.add(new JLabel("Properties File #1: "));
    row1.add(propertiesFile1);
    row1.add(openPropertiesFile1);

    row2.add(new JLabel("Properties File #2: "));
    row2.add(propertiesFile2);
    row2.add(openPropertiesFile2);

    center.add(row1);
    center.add(row2);

    south.add(compare);
    south.add(about);
    south.add(exit);

    Container cp = getContentPane();

    /* Add panels to frame */
    cp.add(BorderLayout.CENTER, center);
    cp.add(BorderLayout.SOUTH, south);

    /* Add the listeners */
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt)
      {
        dispose();
        System.exit(0);
      }
    });

    ImageIcon openIcon = new ImageIcon(ClassLoader.getSystemResource("images/open.gif"));
    openPropertiesFile1.setIcon(openIcon);
    openPropertiesFile2.setIcon(openIcon);

    ImageIcon compareIcon = new ImageIcon(ClassLoader.getSystemResource("images/compare.png"));
    setIconImage(compareIcon.getImage());

    compare.addActionListener(this);
    exit.addActionListener(this);
    about.addActionListener(this);
    openPropertiesFile1.addActionListener(this);
    openPropertiesFile2.addActionListener(this);

    propertiesFile1.setDocument(new CustomDocument());
    propertiesFile2.setDocument(new CustomDocument());

    /* Add items to the menu */
    view.add(look);

    /* Add menu to menu bar */
    mb.add(view);

    /* Set menu bar */
    setJMenuBar(mb);

    /* Determine selected look and feel and ensure it's applied */
    for (JRadioButtonMenuItem item : looks)
    {
      if (item.isSelected())
      {
        item.doClick();
        break;
      }
    }

    setButtonsSize();

    /* Size the frame */
    pack();

    /* Center the frame */
    setLocationRelativeTo(null);

    /* Prevent frame being resized */
    setResizable(false);

    /* Set frame visible */
    setVisible(true);
  }

  public void actionPerformed(ActionEvent evt)
  {
    Object obj = evt.getSource();
    String message = null;

    if (obj == openPropertiesFile1)
    {
      selectFile(propertiesFile1);
    }
    else if (obj == openPropertiesFile2)
    {
      selectFile(propertiesFile2);
    }
    else if (obj == compare)
    {
      /* Load the properties files */
      FileInputStream fis1 = null;
      FileInputStream fis2 = null;

      try
      {
        fis1 = new FileInputStream(propertiesFile1.getText());
        fis2 = new FileInputStream(propertiesFile2.getText());

        Properties prop1 = new Properties();
        Properties prop2 = new Properties();

        prop1.load(fis1);
        prop2.load(fis2);

        Set<Object> prop1Keys = prop1.keySet();
        Set<Object> prop2Keys = prop2.keySet();
        Set<Object> keys = Stream.concat(prop1Keys.stream(), prop2Keys.stream()).collect(Collectors.toSet());

        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Vector<String> columnNames = new Vector<String>();

        columnNames.add("Key");
        columnNames.add("Properties File #1");
        columnNames.add("Properties File #2");

        keys.forEach(key -> {
          Vector<String> row = new Vector<String>();

          String keyStr = (String) key;

          row.add(keyStr);
          row.add(prop1.getProperty(keyStr));
          row.add(prop2.getProperty(keyStr));

          data.add(row);
        });

        new ComparisonResultsDialog(PropertiesFileComparatorFrame.this, columnNames, data);
      }
      catch (IOException ioe)
      {
        message = "Problem reading properties files" + NEW_LINE + ioe.getMessage();
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
      }
      finally
      {
        if (fis1 != null)
        {
          try
          {
            fis1.close();
          }
          catch (IOException e)
          {
            // No blood, no foul!
          }
        }

        if (fis2 != null)
        {
          try
          {
            fis2.close();
          }
          catch (IOException e)
          {
            // No blood, no foul!
          }
        }
      }
    }
    else if (obj == exit)
    {
      dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    else if (obj == about)
    {
      message = "Properties File Comparator" + NEW_LINE + "Compares properties key values" + NEW_LINE
          + "Pat Paternostro";
      JOptionPane.showMessageDialog(this, message, "About Properties File Comparator", JOptionPane.INFORMATION_MESSAGE);
    }
    else if (obj instanceof JRadioButtonMenuItem)
    {
      try
      {
        /*
         * The radio button menu item's action command was set to the associated
         * Look and Feel class name in the constructor.
         */
        AbstractButton ab = ((AbstractButton) obj);
        UIManager.setLookAndFeel(ab.getActionCommand());
        prefs.put(LOOK_AND_FEEL, ab.getText());
        prefs.flush();
        SwingUtilities.updateComponentTreeUI(PropertiesFileComparatorFrame.this);
        setButtonsSize();
        pack();
      }
      catch (final Throwable th)
      {
        JOptionPane.showMessageDialog(PropertiesFileComparatorFrame.this, th.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Sets all buttons to the same size as the largest button.
   */
  private void setButtonsSize()
  {
    Dimension dimension = compare.getPreferredSize();
    about.setPreferredSize(dimension);
    exit.setPreferredSize(dimension);
  }

  /**
   * Sets the selected file name to the specified text field.
   * 
   * @param textField the text field
   */
  private void selectFile(JTextField textField)
  {
    JFileChooser fc = new JFileChooser();

    /* Files only */
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

    /* Disable the "All files" option */
    fc.setAcceptAllFileFilterUsed(false);

    fc.addChoosableFileFilter(new FileNameExtensionFilter("Java Properties Files (*.properties)", "properties"));

    disableTextField(fc);

    int choice = fc.showOpenDialog(this);

    if (choice == JFileChooser.APPROVE_OPTION)
    {
      File selectedFile = fc.getSelectedFile();
      textField.setText(selectedFile.getAbsolutePath());
    }
  }

  /**
   * Disables all text fields in the specified container. Uses recursion to
   * ensure all text fields in the container hierarchy are disabled.
   * 
   * @param container the container
   */
  private void disableTextField(Container container)
  {
    int count = container.getComponentCount();

    for (int i = 0; i < count; i++)
    {
      Component comp = container.getComponent(i);

      if (comp instanceof JTextField)
      {
        JTextField textField = (JTextField) comp;
        textField.setEditable(false);
      }
      else if (comp instanceof Container)
      {
        disableTextField((Container) comp);
      }
    }
  }

  /**
   * Custom document class that enables/disables the 'compare' button based on
   * the absence/presence of text field data.
   * 
   * @version 1.0
   */
  private class CustomDocument extends PlainDocument
  {
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 1238084251315036604L;

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
      super.insertString(offs, str, a);
      compare.setEnabled(propertiesFile1.getText().length() != 0 && propertiesFile2.getText().length() != 0);
    }

    @Override
    public void remove(int offset, int length) throws BadLocationException
    {
      super.remove(offset, length);
      compare.setEnabled(!(propertiesFile1.getText().length() == 0 || propertiesFile2.getText().length() == 0));
    }
  }
}
