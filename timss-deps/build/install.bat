@echo off
rem cd /d %~dp0
set CUR=%~dp0%
 
rem �л����ϼ�Ŀ¼
cd %CUR%/../

echo ��ǰ����������²�������װ�����زֿ���
pause 

call mvn install -Dmaven.test.skip=true


cd %CUR%
pause