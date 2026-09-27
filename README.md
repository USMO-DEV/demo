# demo（前后端分离版）

前后端分离的简单 Java + Vue 3 Web 应用。

## 项目结构
```
demo/
├── backend/                 # 后端：Java HttpServer 纯 API（GET /api/time）
│   ├── src/Main.java
│   ├── Dockerfile           # eclipse-temurin:11 多阶段构建
│   ├── Jenkinsfile          # 后端流水线（可独立成仓库）
│   ├── .dockerignore
│   └── run.bat              # 本地运行（需 JDK 11+）
├── frontend/                # 前端：Vue 3 + Vite
│   ├── src/  index.html  package.json  vite.config.js
│   ├── nginx.conf           # 静态托管 + /api 反代到后端容器
│   ├── Dockerfile           # node 构建 + nginx 运行
│   ├── Jenkinsfile          # 前端流水线（可独立成仓库）
│   ├── docker-compose.yml   # 前后端整体编排（拆仓后随前端仓库走）
│   └── .dockerignore
```

## 仓库间约定（拆分到两个仓库后遵守）
- 共用 Docker 网络 `demo-net`
- 后端容器固定别名 `backend`，监听容器端口 `8080`，不对外暴露
- 前端 nginx 通过 `http://backend:8080` 反代 `/api`，对外暴露宿主机 `8888`

## 本地运行（Docker Compose）
```bash
docker compose up -d --build
# 前端页面:  http://localhost:8888
# 后端 API:  http://localhost:8888/api/time （经前端 nginx 反代）
```

## 本地开发（不走 Docker）
```bash
# 终端 1：启动后端（需 JDK 11+）
cd backend && run.bat

# 终端 2：启动前端（需 Node 18+）
cd frontend
npm install --registry=https://registry.npmmirror.com
npm run dev     # http://localhost:5173，/api 自动代理到 8080
```

## Jenkins 流水线
前后端各有一条独立流水线（`backend/Jenkinsfile`、`frontend/Jenkinsfile`），各自构建镜像、推送到本地仓库 `localhost:5000`、部署替换自己的容器：
- `demo-backend`：后端容器（网络别名 `backend`，仅内网，不暴露宿主机端口）
- `demo-frontend`：前端容器，宿主机 `8888` → 容器 80

当前在一个仓库时，在 Jenkins 建两个流水线任务，脚本路径分别填 `backend/Jenkinsfile` 和 `frontend/Jenkinsfile`；拆分成两个仓库后，脚本路径都改回 `Jenkinsfile` 即可，无需改内容。

基础镜像取自本地私有仓库 `localhost:5000`（需预先推入）：
```bash
docker run -d --name registry --restart unless-stopped -p 5000:5000 registry:2
for img in node:20-alpine eclipse-temurin:11-jdk eclipse-temurin:11-jre nginx:alpine; do
  docker pull $img
  docker tag $img localhost:5000/$img
  docker push localhost:5000/$img
done
```
