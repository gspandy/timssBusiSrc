@echo off 

title Timss-2.0 - 自动打包工具
rem cd /d %~dp0
set BASE_DIR=%cd%
set WORK_DIR=%BASE_DIR%\..\

rem 进入跟目录，循环调用

rem 更新本地的版本信息
echo.
echo   现在开始进行打包动作...
echo
echo   1)  -  将执行强制更新所有的Jar (推荐).
echo   2)  -  将跳过所有的单元测试过程,请单独进行测试.
echo %WORK_DIR%

cd %WORK_DIR%
echo %cd%
rem call mvn clean compile deploy  -Dmaven.test.skip=true -Ppackage -U
call mvn clean compile package -Ppkg,schedule -Dmaven.test.skip=true -U
cd %BASE_DIR%
 

echo 打包完成。
pause