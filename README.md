Prerequisites
Java JDK: Ensure that Java JDK is installed on your system.
JavaFX: Download and install JavaFX.

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

4. Copy the JRE lib Directory to the jre Folder
  After downloading and setting up the JRE, you need to include the necessary libraries in your project. Here’s how you can do it:

  Locate the JRE Installation Directory:

  Windows:
    Open File Explorer.
    Navigate to the drive where Java is installed (typically C:\).
    Go to Program Files (or Program Files (x86) if you installed a 32-bit version).
    Open the Java folder.
    Inside the Java folder, you’ll see one or more folders, each representing a different Java version. Open the folder corresponding to your JRE installation (e.g., jre1.8.0_281 or jre-11).

  MacOS:
    Open Finder.
    In the menu bar, click on Go and select Go to Folder....
    Type /Library/Java/JavaVirtualMachines/ and click Go.
    Open the folder corresponding to your JRE installation (e.g., jdk1.8.0_281.jdk/Contents/Home or jdk-11.jdk/Contents/Home).

  Linux:
    Open a terminal.
    Type echo $JAVA_HOME and press Enter. This will show the path to your JRE installation.
    Use the cd command to navigate to this directory (e.g., cd /usr/lib/jvm/java-11-openjdk-amd64).

  Locate the lib Folder:
  Once inside the JRE folder (e.g., jre1.8.0_281 or jdk-11), look for a folder named lib. This folder contains essential libraries that the JRE uses to run Java applications.
  Copy the lib Folder:

  Windows:
    Right-click on the lib folder and select Copy.
    Navigate to your project directory where the jre folder is located.
    Right-click inside the jre directory and select Paste.

  MacOS:
    Right-click (or Control-click) on the lib folder and select Copy.
    Navigate to your project directory where the jre folder is located.
    Right-click (or Control-click) inside the jre directory and select Paste Item.

  Linux:
    Use the cp command to copy the lib folder. For example:
    bash
    Copy code
    cp -r /usr/lib/jvm/java-11-openjdk-amd64/lib /path/to/your/project/jre/
    Replace /path/to/your/project/ with the actual path to your project directory.

  Verify the Copy:
  After pasting the lib folder, double-check to ensure that it has been successfully copied into the jre folder within your project.
  This process ensures that the necessary JRE libraries are bundled with your application, allowing it to run independently of the user's system Java installation.

5. Running the Application
  Option 1: Using the Executable
    Simply run the AirlineSystem.exe file located in the root directory.

  Option 2: Using the JAR File
  If you prefer to run the application via the JAR file:
    java --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -cp bin AirlineSystem

  Option 3: Compile and Run Using the Source Code
  If you want to compile and run the application from the source code:
    javac --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -d bin src/*.java
    java --module-path "REPLACE THIS WITH YOUR PATH TO JAVAFX LIB" --add-modules javafx.controls,javafx.fxml -cp bin AirlineSystem

6. Done.