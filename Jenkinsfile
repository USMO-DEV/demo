// Jenkins 声明式流水线：构建 Docker 镜像并推送（可选）
pipeline {
    agent any

    triggers {
        pollSCM('H/1 * * * *')
    }

    environment {
        // 镜像名称：仓库名/应用名，按需修改
        IMAGE_NAME   = 'demo-java-app'
        // 镜像标签：使用构建号 + 提交 ID
        IMAGE_TAG    = "${BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'local'}"
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

        stage('编译验证') {
            steps {
                sh 'javac -version || true'
                echo "JDK 检查完成"
            }
        }

        stage('构建镜像') {
            steps {
                script {
                    def image = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                    env.FULL_IMAGE = "${IMAGE_NAME}:${IMAGE_TAG}"
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
