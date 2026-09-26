@echo off
chcp 65001 >nul
cd /d %~dp0

if not exist "out" mkdir out

echo [1/2] 编译中...
javac -encoding UTF-8 -d out src\Main.java
if errorlevel 1 (
    echo 编译失败，请确认已安装 JDK 并配置好环境变量。
    pause
    exit /b 1
)

echo [2/2] 启动服务...
java -cp out Main
pause
