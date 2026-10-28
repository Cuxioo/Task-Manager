@echo off
setlocal
cd /d "%~dp0"

where mvn >nul 2>nul
if errorlevel 1 (
    echo.
    echo Maven is required to run the JavaFX app.
    echo Install Apache Maven and ensure "mvn" is on your PATH.
    pause
    exit /b 1
)

mvn javafx:run
set "exitCode=%ERRORLEVEL%"
pause
exit /b %exitCode%