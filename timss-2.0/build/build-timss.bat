@echo off 

rem cd /d %~dp0
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%/../
SET TIMSS=%WORK_DIR%/../

set WEB_XML=%WORK_DIR%/src/main/webapp/WEB-INF/web.xml
set FRAGMENT_XML=%WORK_DIR%/target/web-fragment.xml

rem �����Ŀ¼��ѭ������
cd /d %TIMSS%/
rem ���±��صİ汾��Ϣ

echo ���±���������Ϣ��TIMSS :: Deps
rem call  %TIMSS%/timss-deps/build/install.bat


echo ���FRAMWRORKģ��
call :FRAMWRORK

echo ���WORKFLOWģ��
call :WORKFLOW

GOTO END 


:FRAMWRORK
echo ����framework
cd %TIMSS%\core-framework
mvn clean compile install -Dmaven.test.skip=true
goto :eof



:WORKFLOW
setlocal
echo ����workflow
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
rem call mvn dependency:copy-dependencies �CPsnapshot -DoutputDirectory=target/dependencies -DincludeScope=compile

rem call %CUR%/bin/sed.exe -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%
rem if exist %WEB_XML%	(call %CUR%/bin/sed.exe -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%) else (echo NOT FOUND)

rem call mvn clean package
:END 
cd %BASE_DIR%
pause