@echo off 

title Timss-2.0 - �Զ��������
rem cd /d %~dp0
set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\

rem �����Ŀ¼��ѭ������

rem ���±��صİ汾��Ϣ
echo.
echo   ���ڿ�ʼ���д������...
echo
echo   1)  -  ��ִ��ǿ�Ƹ������е�Jar (�Ƽ�).
echo   2)  -  ���������еĵ�Ԫ���Թ���,�뵥�����в���.
echo %WORK_DIR%

cd %WORK_DIR%
echo %cd%
rem call mvn clean compile deploy  -Dmaven.test.skip=true -Ppackage -U
call mvn clean compile package -Ppkg,schedule -Dmaven.test.skip=true -U
cd %BASE_DIR%
 

echo �����ɡ�
pause