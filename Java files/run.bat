@echo off
set PATH_TO_FX="C:\Users\user\javafx-sdk-22.0.2\lib"
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml *.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml AirlineSystem
pause
