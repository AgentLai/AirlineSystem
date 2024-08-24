Prerequisites
Java JDK: Ensure that Java JDK is installed on your system.
JavaFX: Download and install JavaFX.

Default Credentials

Admin Credentials:
Username: Admin
Password: Admin123

Staff Credentials:
Username: Staff
Password: Staff123

Setup Instructions
1. Download and Install Required Software
Download and install Java JDK.
Download and install JavaFX.

2. Directory Structure
Your project should have the following directory structure:

lib/: Contains JavaFX libraries.
src/: Contains the .java source files.
jre/: Contains the JRE files for distribution.
bin/: Contains the compiled .class files.
AirlineSystem.exe: The executable file to run the application.
AirlineSystem.jar: The JAR file containing the application.

3. Compiling the Code
To compile the code, open your terminal or command prompt and navigate to the src directory containing the .java files. Use the following command to compile the project:
  javac --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -d ../bin *.java
Replace "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" with the actual path to your JavaFX lib directory.

4. Running the Application
Option 1: Using the Executable
  Simply run the AirlineSystem.exe file located in the root directory.

Option 2: Using the JAR File
If you prefer to run the application via the JAR file:
  java --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -cp bin AirlineSystem

Option 3: Compile and Run Using the Source Code
If you want to compile and run the application from the source code:
  javac --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -d bin src/*.java
  java --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -cp bin AirlineSystem

5. Done.