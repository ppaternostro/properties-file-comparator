# properties-file-comparator

A properties/YML file comparator utility.

This application allows the user to choose two different properties/YML files in order to compare those files' key values. 

Upon application startup you will be presented with the following window:

<img width="661" height="268" alt="Main Window" src="https://github.com/user-attachments/assets/03846ead-e918-470e-87c1-054232290ff0" />

Select each properties/YML file by clicking the folder icon. Once both files have been selected the **Compare...** button becomes enabled. Click that button to display the **Comparison Results** dialog. 

<img width="615" height="330" alt="Comparison Results" src="https://github.com/user-attachments/assets/cf15eb08-7014-41fc-af91-3ca81e859eea" />

The comparison results are displayed via a table. If the key values are different, or if a key exists in one file and not the other, the row will be red highlighted with the different key values displayed for each file. If the key values are the same, the row will be green highlighted. You can choose to only display the key value differences to reduce the output noise via the **Only show differences** check box. 

<img width="615" height="330" alt="Only Show Differences" src="https://github.com/user-attachments/assets/be8deb6e-71de-4609-b543-ea1e33674058" />

Clicking on the table column headers sorts the data in ascending/descending order based on that column's data. The data is automatically sorted by key name in ascending order (note the black triangle icon in the **Key** column header).

The **Save...** button allows the user to persist the comparison results via a comma separated value (CSV) file.

## Build

To build the application, in the project's root folder, execute the following from a terminal window

> mvnw package (use **./mvnw** for Unix/Linux based OSes)

The command will create the following two JAR files in the project's root folder **target** directory.

- properties-file-comparator-0.0.1-SNAPSHOT.jar
- properties-file-comparator-0.0.1-SNAPSHOT-jar-with-dependencies.jar

The **properties-file-comparator-0.0.1-SNAPSHOT-jar-with-dependencies.jar** file is an executable JAR that includes all the project's dependencies.

Run the following from the terminal window to execute the application.

```bash
java -jar properties-file-comparator-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```