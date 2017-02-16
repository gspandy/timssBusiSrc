@echo off 
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%
call mvn clean compile -U

cd %BASE_DIR%
pause