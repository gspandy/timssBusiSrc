@echo off
rem cd /d %~dp0
set CUR=%~dp0%
 
rem �л����ϼ�Ŀ¼
cd %CUR%/../

echo ��ǰ����������²��������𵽹�˾ͳһ�Ĳֿ���
pause 

call mvn deploy -Dmaven.test.skip=true


cd %CUR%
pause