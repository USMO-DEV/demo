# demo

一个简单的 Java Web 应用，使用 JDK 内置 HttpServer，无任何第三方依赖。

## 运行方式

### Windows
直接双击 `run.bat`，浏览器会自动打开 http://localhost:8080

### 手动运行
```bash
javac -encoding UTF-8 -d out src/Main.java
java -cp out Main
```

## 项目结构
```
demo/
├── src/Main.java      # Java 后端（静态页面 + API）
├── web/index.html     # 前端页面
├── out/               # 编译输出
└── run.bat            # 一键启动脚本
```

## 功能
- 首页：`http://localhost:8080`
- 接口：`http://localhost:8080/api/time` 返回服务器当前时间（JSON）
