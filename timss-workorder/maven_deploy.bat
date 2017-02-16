@echo on
echo 开始打包并自动提交
mvn deploy -Dmaven.test.skip=true
echo 打包完毕
pause