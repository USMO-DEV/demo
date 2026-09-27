// Jenkins 声明式流水线：构建 Docker 镜像并推送（可选）
pipeline {
    agent any


    environment {
        // 镜像名称：仓库名/应用名，按需修改
        IMAGE_NAME   = 'demo-java-app'
        // 镜像标签：使用构建号 + 提交 ID
        IMAGE_TAG    = "${BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'local'}"
        // 基础镜像仓库：使用服务器本地的私有仓库，构建时不访问外网
        // （需先在服务器上启动 registry 并推入基础镜像；如需直连公网改回 docker.io/library）
        BASE_REGISTRY = 'localhost:5000'
        // Docker 镜像仓库地址（如需推送，取消注释并修改）
        // REGISTRY    = 'registry.example.com'
        // CREDENTIALS = 'docker-registry-credentials-id'
    }

    stages {
        stage('检出代码') {
            steps {
                checkout scm
            }
        }

        stage('构建镜像') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}", "--build-arg BASE_REGISTRY=${BASE_REGISTRY} .")
                    env.FULL_IMAGE = "${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('部署运行') {
            steps {
                script {
                    // 1. 停止并删除旧容器
                    sh 'docker rm -f demo || true'

                    // 2. 删除上一次构建留下的旧镜像（保留本次新镜像）
                    sh '''
                        docker images --format "{{.Repository}}:{{.Tag}}" ${IMAGE_NAME} \
                          | grep -v "${IMAGE_TAG}" \
                          | xargs -r docker rmi -f || true
                    '''

                    // 3. 运行新容器：宿主机 8888 -> 容器 8080（8080 已被 Jenkins 占用）
                    sh 'docker run -d --name demo -p 80:8080 --restart unless-stopped ${FULL_IMAGE}'

                    // 4. 健康检查
                    sh 'sleep 3'
                    sh 'curl -sf http://localhost:80/api/time && echo " <- 服务正常"'
                }
            }
        }
    }

    post {
        always {
            // 清理本次构建产生的镜像，避免磁盘堆积
            //sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true'
            cleanWs()
        }
        success {
            echo "构建成功：${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo '构建失败，请查看日志777777'
        }
    }
}
