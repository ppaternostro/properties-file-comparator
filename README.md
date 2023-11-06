# properties-file-comparator
A properties file comparator utility.

This application allows the user to choose two different properties files in order to compare those files' key values. 

Upon application startup you will be presented with the following window:

![Main Window](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/9c8438a1-c874-4ce2-b702-b5da88a2759f)

Select each properties file by clicking the folder icon. Once both files have been selected the **Compare...** button becomes enabled. Click that button to display the **Comparison Results** dialog. 

![Comparison Results](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/36df733a-81d5-4abb-b3f3-9c3235641858)

The comparison results are displayed via a table. If the key values are different, or if a key exists in one file and not the other, the row will be red highlighted with the different key values displayed for each file. If the key values are the same, the row will be green highlighted. You can choose to only display the key value differences to reduce the output noise via the **Only show differences** check box. 

![Only Show Differences](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/c66251e7-89c5-430e-a8dd-7ea9e28442e0)

Clicking on the table column headers sorts the data in ascending/descending order based on that column's data. The data is automatically sorted by key name in ascending order (note the black triangle icon in the **Key** column header).

The **Save...** button allows the user to persist the comparison results via a comma separated value (CSV) file.