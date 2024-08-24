@echo off
REM Compile the Java files
javac --module-path "D:\Programming\Tools\Random Programming Junk\openjfx-22.0.2_windows-x64_bin-sdk\javafx-sdk-22.0.2\lib" --add-modules javafx.controls,javafx.fxml *.java

REM Run the compiled Java program
java --module-path "D:\Programming\Tools\Random Programming Junk\openjfx-22.0.2_windows-x64_bin-sdk\javafx-sdk-22.0.2\lib" --add-modules javafx.controls,javafx.fxml AirlineSystem
