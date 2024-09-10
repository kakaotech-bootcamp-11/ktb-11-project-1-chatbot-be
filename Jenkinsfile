pipeline {
    agent none
    environment {
        DOCKER_REPO = 'ktb11chatbot/ktb-11-project-1-chatbot-be'
        GIT_BRANCH = 'feature/jenkins'  // 빌드할 Git 브랜치
        K8S_NAMESPACE = 'devops-tools'  // 배포할 네임스페이스
        KANIKO_POD_YAML = '/home/ubuntu/kaniko/kaniko-pod-be.yaml'  // Kaniko Pod YAML 파일 경로
    }
    stages {
        stage('Checkout Source Code') {
            agent any
            steps {
                // Git 소스 코드를 체크아웃
                checkout scm
                script {
                    env.GIT_COMMIT_SHORT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Current Git Commit Short: ${env.GIT_COMMIT_SHORT}" // Git 커밋 ID 앞 7자리
                }
            }
        }
        stage('Update Kaniko YAML') {
            agent { label master }
                    steps {
                        script {
                            // 이미지 태그를 생성하고, kaniko-pod-be.yaml 파일을 동적으로 수정
                            sh """
                            sed -i 's|--destination=.*|--destination=docker.io/${DOCKER_REPO}:${GIT_COMMIT_SHORT}|' ${KANIKO_POD_YAML}
                            """
                        }
                    }
                }
        stage('Deploy Kaniko Pod') {
        agent any
                    steps {
                        script {
                            // 동적으로 수정된 Kaniko Pod YAML 파일을 Kubernetes에 적용
                            sh """
                            kubectl apply -f ${KANIKO_POD_YAML} -n ${K8S_NAMESPACE}
                            """
                        }
                    }
                }
        stage('Wait for Build Completion') {
        agent any
            steps {
                script {
                    // Kaniko Pod 빌드 완료 대기
                    sh """
                    kubectl wait --for=condition=complete pod/kaniko-backend -n ${K8S_NAMESPACE} --timeout=600s
                    """
                }
            }
        }
        stage('Deploy to Kubernetes') {
            agent any
            steps {
                script {
                    // Kubernetes 배포
                    sh """
                    kubectl set image deployment/backend-deployment \
                    -n ${K8S_NAMESPACE} backend=docker.io/${DOCKER_REPO}:${GIT_COMMIT_SHORT}
                    kubectl rollout status deployment/backend-deployment -n ${K8S_NAMESPACE}
                    """
                }
            }
        }
    }
    post {
            success {
                echo 'Build and push successful!'
            }
            failure {
                echo 'Build or deployment failed. Check logs for details.'
                angent any
                script {
                    // Kaniko Pod의 로그 확인
                    sh """
                    kubectl logs pod/kaniko-backend -n ${K8S_NAMESPACE}
                    """
            }
        }
    }
}
