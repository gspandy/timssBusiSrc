@echo off 
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%/../
set WEB_XML=%WORK_DIR%/src/main/webapp/WEB-INF/web.xml
set FRAGMENT_XML=%WORK_DIR%/target/web-fragment.xml
rem 文本替换工具，解决不断出现将web.xml -> web-fragment.xml
set SED_PATH=%WORK_DIR%/../timss-deps/build/bin/sed.exe
set AWK_PATH=%WORK_DIR%/../timss-deps/build/bin/awk.exe

rem call mvn dependency:copy-dependencies -DoutputDirectory=target/dependencies -DincludeScope=compile
rem call mvn dependency:copy-dependencies –Psnapshot -DoutputDirectory=target/dependencies -DincludeScope=compile

cd %WORK_DIR%
call mvn clean compile deploy -Ppkg -Dmaven.test.skip=true -U

:END
cd %BASE_DIR%
PAUSE