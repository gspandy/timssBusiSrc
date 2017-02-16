@echo off
rem cd /d %~dp0
set CUR=%~dp0%
 
rem 切换到上级目录
cd %CUR%/../

echo 当前命令将进行如下操作：安装到本地仓库中
pause 

call mvn install -Dmaven.test.skip=true


cd %CUR%
pause