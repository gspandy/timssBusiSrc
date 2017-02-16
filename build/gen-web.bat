@echo off 
set BASE_DIR=%~dp0%
set WORK_DIR=%BASE_DIR%\..\
set WEB_XML=%WORK_DIR%/src/main/webapp/WEB-INF/web.xml
set FRAGMENT_XML=%WORK_DIR%/target/web-fragment.xml
rem �ı��滻���ߣ�������ϳ��ֽ�web.xml -> web-fragment.xml
set SED_PATH=%WORK_DIR%/timss-deps/build/bin/sed.exe
set AWK_PATH=%WORK_DIR%/timss-deps/build/bin/awk.exe

rem call mvn dependency:copy-dependencies -DoutputDirectory=target/dependencies -DincludeScope=compile
rem call mvn dependency:copy-dependencies �CPsnapshot -DoutputDirectory=target/dependencies -DincludeScope=compile

cd %WORK_DIR%

rem call %CUR%/bin/sed.exe -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%
if exist %WEB_XML%	(
call %SED_PATH% -e "s/web-app/web-fragment/" %WEB_XML% > %FRAGMENT_XML%
) else (
copy "%WORK_DIR%\timss-deps\build\web-fragment.xml" "%FRAGMENT_XML%"
)
