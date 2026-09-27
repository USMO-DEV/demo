# 基础镜像仓库，国内网络无法直连 Docker Hub 时可通过 --build-arg 切换加速源
# 例如: --build-arg BASE_REGISTRY=docker.xuanyuan.me/library
ARG BASE_REGISTRY=docker.io/library

# ---------- 阶段 1：Node 环境构建 Vue 3 前端 ----------
FROM ${BASE_REGISTRY}/node:20-alpine AS frontend
WORKDIR /app/frontend

# 先复制依赖清单，利用 Docker 层缓存
COPY frontend/package.json ./
RUN npm install --registry=https://registry.npmmirror.com

COPY frontend/ ./
# 构建产物输出到 dist/
RUN npm run build

# ---------- 阶段 2：JDK 编译 Java 源码 ----------
FROM ${BASE_REGISTRY}/eclipse-temurin:11-jdk AS build
WORKDIR /app
COPY src/Main.java ./
RUN javac -encoding UTF-8 Main.java

# ---------- 阶段 3：JRE 运行 ----------
FROM ${BASE_REGISTRY}/eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/*.class ./
# Vue 构建产物放到 Java 服务的 web 目录，由 Java 提供静态文件服务
COPY --from=frontend /app/frontend/dist ./web

EXPOSE 8080
CMD ["java", "Main"]
