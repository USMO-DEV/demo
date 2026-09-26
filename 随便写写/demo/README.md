# Java Web Demo

一个零依赖的 Java Web 项目，使用 JDK 内置的 `com.sun.net.httpserver.HttpServer`，无需 Maven/Gradle。

## 项目结构

```
demo/
├── src/Main.java      # 服务端代码
├── web/index.html     # 静态首页
├── run.bat            # 一键编译并运行（Windows）
└── out/               # 编译输出（运行后自动生成）
```

## 运行

前提：已安装 JDK 11+ 并配置好 `java`/`javac` 环境变量。

双击 `run.bat`，或在项目目录执行：

```bat
javac -encoding UTF-8 -d out src\Main.java
java -cp out Main
```

## 访问

| 地址 | 说明 |
|------|------|
| http://localhost:8080/ | 静态首页（可输入名字调用接口） |
| http://localhost:8080/api/hello?name=张三 | JSON 接口，返回问候与当前时间 |
| http://localhost:8080/api/health | 健康检查 |

修改 `src/Main.java` 中的 `PORT` 常量可更换端口。
