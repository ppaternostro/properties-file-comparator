# properties-file-comparator
A properties file comparator utility.

This application allows the user to choose two different properties files to compare those files' key values. 

Upon application startup you will be presented with the following window:

![Main Window](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/149c7fc8-4beb-41f8-a49a-f730e8211934)

Select each properties file by clicking the folder icon. Once both files have been selected the **Compare...** button becomes enabled. Click that button to display the **Comparison Results** dialog. 

![Comparison Results](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/3d96ccb9-e98a-4635-82d6-11edb43f4fd2)

The comparison results are displayed via a table. If the key values are different, or if a key exists in one file and not the other, the row will be red highlighted with the different key values displayed for each file. If the key values are the same, the row will be green highlighted. You can choose to only display the key value differences to reduce the output noise via the **Only show differences** check box. 

![Only Differences](https://github.com/ppaternostro/properties-file-comparator/assets/32653184/c97141b6-cbe4-414e-af2a-51cb2c7ae73d)

The **Save...** button allows the user to persist the comparison results via a comma separated value (CSV) file.