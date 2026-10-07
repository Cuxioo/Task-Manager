@echo off
setlocal
cd /d "%~dp0"

if not exist "build\classes" mkdir "build\classes"

javac -cp "lib/*" -d "build\classes" "src\Main.java" "src\DatabaseManager.java" "src\Task.java"
if errorlevel 1 (
    echo.
    echo Failed to compile Task Manager.
    pause
    exit /b 1
)

java -cp "build\classes;lib/*" src.Main
set "exitCode=%ERRORLEVEL%"
pause
exit /b %exitCode%