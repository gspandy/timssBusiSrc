@echo off 
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%\..\
cd %WORK_DIR%

call mvn clean compile tomcat7:run -Dmaven.test.skip=true -Prun-tomcat

cd %BASE_DIR%
pause

