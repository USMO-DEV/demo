ARG BASE_REGISTRY=docker.io/library

# ---------- 构建阶段：编译 Vue 3 ----------
FROM ${BASE_REGISTRY}/node:20-alpine AS build
WORKDIR /app

# 先复制依赖清单，利用 Docker 层缓存
COPY package.json ./
RUN npm install --registry=https://registry.npmmirror.com

COPY . ./
RUN npm run build

# ---------- 运行阶段：nginx 托管静态文件并代理 API ----------
FROM ${BASE_REGISTRY}/nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
