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

## Jenkins 自动构建

流水线文件为 `Jenkinsfile`，构建流程：检出 → 构建镜像 → 推送（仅 master 分支）→ 容器验证。

### Jenkins 侧准备
1. Jenkins 需安装 **Docker Pipeline**（或 Docker 插件）和 **Pipeline** 插件
2. Jenkins 用户需有 Docker 权限（`usermod -aG docker jenkins`）
3. 如需推送镜像仓库，在 Jenkins 凭据中添加 Docker 仓库账号密码，ID 设为 `docker-registry-credentials-id`，并修改 `Jenkinsfile` 中的 `REGISTRY` 地址

### 创建流水线
新建任务 → 流水线（Pipeline）→ Pipeline script from SCM → 指定 Git 仓库地址，脚本路径填 `Jenkinsfile`。

### 本地测试镜像
```bash
docker build -t demo-java-app .
docker run -d -p 8080:8080 --name demo demo-java-app
# 访问 http://localhost:8080
```

