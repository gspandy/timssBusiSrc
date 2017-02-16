@echo off
rem cd /d %~dp0
set CUR=%~dp0%
 
rem 切换到上级目录
cd %CUR%/../

echo 当前命令将进行如下操作：部署到公司统一的仓库中
pause 

call mvn deploy -Dmaven.test.skip=true


cd %CUR%
pause