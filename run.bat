@echo off
REM Set the paths
set SRC_PATH=D:\Programming\Github\Repository\AirlineSystem\src
set BIN_PATH=D:\Programming\Github\Repository\AirlineSystem\bin
set LIB_PATH=D:\Programming\Github\Repository\AirlineSystem\lib

REM Clean the bin directory (optional)
if exist "%BIN_PATH%" (
    echo Cleaning bin directory...
    rmdir /s /q "%BIN_PATH%"
)
mkdir "%BIN_PATH%"

REM Compile the Java files
echo Compiling Java files...
javac --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml -d "%BIN_PATH%" "%SRC_PATH%\*.java"
if %errorlevel% neq 0 (
    echo Compilation failed. Press any key to exit.
    pause >nul
    exit /b %errorlevel%
)

REM Run the Java program
echo Running AirlineSystem...
java --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml -Djava.library.path="%LIB_PATH%" -cp "%BIN_PATH%" AirlineSystem
if %errorlevel% neq 0 (
    echo Error: Failed to run AirlineSystem. Press any key to exit.
    pause >nul
    exit /b %errorlevel%
)

echo Program executed successfully. Press any key to exit.
pause >nul
