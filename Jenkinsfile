pipeline {
    agent none
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')  // Docker Hub 인증 정보
        DOCKER_REPO = 'ktb11chatbot/ktb-11-project-1-chatbot-be'
        GIT_BRANCH = 'feature/jenkins'  // 빌드할 Git 브랜치
        K8S_NAMESPACE = 'devops-tools'  // 배포할 네임스페이스
        GIT_COMMIT_SHORT = "${env.GIT_COMMIT.take(7)}"  // Git 커밋 ID 앞 7자리
    }
    stages {
        stage('Build and Push with Kaniko') {
            agent {
                kubernetes {
                    label 'kaniko-build'
                    defaultContainer 'kaniko'
                    yaml """
apiVersion: v1
kind: Pod
metadata:
  namespace: ${K8S_NAMESPACE}
  name: kaniko-backend
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    args:
    - --dockerfile=./Dockerfile
    - --context=git://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@github.com/kakaotech-bootcamp-11/ktb-11-project-1-chatbot-be.git#refs/heads/${GIT_BRANCH}
    - --destination=docker.io/${DOCKER_REPO}:${GIT_COMMIT_SHORT}
    volumeMounts:
    - name: kaniko-secret
      mountPath: /kaniko/.docker
    - name: dockerfile-storage
      mountPath: /workspace
  restartPolicy: Never
  volumes:
  - name: kaniko-secret
    secret:
      secretName: regcred
      items:
        - key: .dockerconfigjson
          path: config.json
  - name: dockerfile-storage
    persistentVolumeClaim:
      claimName: kaniko-pv-claim
"""
                }
            }
            steps {
                container('kaniko') {
                    script {
                        echo "Building and pushing Docker image: ${DOCKER_REPO}:${GIT_COMMIT_SHORT}"
                    }
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
            echo 'Build, push, and deploy successful!'
        }
        failure {
            echo 'Build or deployment failed. Check logs for details.'
        }
    }
}