Default admin credentials:
Username: Admin
Password: Admin123

Default staff credentials:
Username: Staff
Password: Staff123

Prerequisites
Java JDK: Ensure that Java JDK is installed on your system.
JavaFX: Download and install JavaFX.

Setup Instructions
Download and install Java JDK and JavaFX.
Replace "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" with the actual path to your JavaFX lib directory in the commands provided below.

To compile the code, open your terminal or command prompt and navigate to the directory containing the .java files. Use the following command to compile the project:

javac --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml *.java
------------------------------------------------------------------------------------------------------------------------------------------------
Once compiled, you can run the application using the following command:

java --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml AirlineSystem