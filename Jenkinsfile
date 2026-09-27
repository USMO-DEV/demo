// 前端流水线（独立仓库可用，只负责 Vue + nginx 镜像）
// 与后端仓库的约定：通过 docker 网络 demo-net 的别名 backend 访问后端 8080 端口
pipeline {
    agent any

    environment {
        IMAGE_NAME     = 'demo-web'
        IMAGE_TAG      = "${BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'local'}"
        // 基础镜像仓库：已预推入 node:20-alpine / nginx:alpine
        BASE_REGISTRY  = 'localhost:5000'
        NET_NAME       = 'demo-net'
        CONTAINER      = 'demo-frontend'
    }

    stages {
        stage('检查代码') {
            steps {
                checkout scm
            }
        }

        stage('构建镜像') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}",
                                 "-f Dockerfile --build-arg BASE_REGISTRY=${BASE_REGISTRY} .")
                }
            }
        }

        stage('部署运行') {
            steps {
                script {
                    // 1. 容器网络（与后端同一网络）
                    sh "docker network create ${NET_NAME} 2>/dev/null || true"

                    // 2. 替换容器：宿主机 8888 -> 容器 80（8080 已被 Jenkins 占用）
                    sh "docker rm -f ${CONTAINER} || true"
                    sh "docker run -d --name ${CONTAINER} --network ${NET_NAME} -p 8888:80 --restart unless-stopped ${IMAGE_NAME}:${IMAGE_TAG}"

                    // 3. 清理本仓库的旧镜像（保留本次新镜像）
                    sh '''
                        docker images --format "{{.Repository}}:{{.Tag}}" ${IMAGE_NAME} \
                          | grep -v "${IMAGE_TAG}" \
                          | xargs -r docker rmi -f || true
                    '''

                    // 4. 健康检查（顺带验证到后端的反代链路；后端未部署时会失败）
                    sh 'sleep 1'
                    sh 'curl -sf http://localhost:8888/api/time && echo " <- 前后端链路正常"'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "前端部署成功：${IMAGE_NAME}:${IMAGE_TAG}，访问 http://<服务器IP>:8888"
        }
        failure {
            echo '前端构建失败，请查看日志'
        }
    }
}
