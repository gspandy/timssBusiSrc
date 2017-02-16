@echo off 

rem cd /d %~dp0
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%/../
SET TIMSS=%WORK_DIR%/../

set WEB_XML=%WORK_DIR%/src/main/webapp/WEB-INF/web.xml
set FRAGMENT_XML=%WORK_DIR%/target/web-fragment.xml

rem 进入跟目录，循环调用
cd /d %TIMSS%/
rem 更新本地的版本信息

echo 更新本地依赖信息：TIMSS :: Deps
rem call  %TIMSS%/timss-deps/build/install.bat


echo 打包FRAMWRORK模块
call :FRAMWRORK

echo 打包WORKFLOW模块
call :WORKFLOW

GOTO END 


:FRAMWRORK
echo 进入framework
cd %TIMSS%\core-framework
mvn clean compile install -Dmaven.test.skip=true
goto :eof



:WORKFLOW
setlocal
echo 进入workflow
goto :eof



:ASSET

:FINANCE

:HOMEPAGE

:INVENTORY

:OPERATION

:PMS

:PTW

:PURCHASE

:WORKORDER





cd %WORK_DIR%
rem call mvn dependency:copy-dependencies -DoutputDirectory=target/dependencies -DincludeScope=compile
rem call mvn dependency:copy-dependencies –Psnapshot -DoutputDirectory=target/dependencies -DincludeScope=compile

rem call %CUR%/bin/sed.exe -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%
rem if exist %WEB_XML%	(call %CUR%/bin/sed.exe -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%) else (echo NOT FOUND)

rem call mvn clean package
:END 
cd %BASE_DIR%
pause