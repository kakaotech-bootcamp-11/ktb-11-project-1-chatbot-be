pipeline {
    agent any
    environment {
            DOCKER_REPO = "ktb11chatbot/ktb-11-project-1-chatbot-be"
            GIT_BRANCH = 'main'  // 빌드할 Git 브랜치
            JENKINS_NAMESPACE = 'devops-tools'  // kaniko로 build 할때 사용할 네임스페이스 보통 jenkins와 같은 namespace에서 함
            KANIKO_POD_YAML = '/var/jenkins_home/kaniko/backend-kaniko-ci.yaml' // Kaniko Pod YAML 파일 경로
            // KANIKO_POD_YAML NFS.dir path 생성후에 그 안에 Kaniko-ci.yaml을 넣어줘야 함
            KANIKO_POD_NAME = 'kaniko-backend'
            DEPLOYMENT_NAMESPACE = 'ktb-chatbot'
            DEPLOYMENT_NAME = 'backend-deployment'
            DEPLOYMENT_CONTAINER_NAME = 'backend'
        }
    stages {
            stage('Checkout Source Code') {
                steps {
                    // Git 소스 코드 체크아웃
                    checkout scm
                    script {
                        env.GIT_COMMIT_SHORT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                        echo "Current Git Commit Short: ${env.GIT_COMMIT_SHORT}" // Git 커밋 ID의 앞 7자를 태그로 사용

                        env.GIT_COMMIT_MESSAGE = sh(script: 'git log --no-merges -1 --pretty=%B', returnStdout: true).trim()
                        echo "Git Commit Message: ${env.GIT_COMMIT_MESSAGE}"
                    }
                }
            }
            /*
            stage('Update Kaniko YAML') {
                steps {
                    script {
                        // Kaniko YAML 파일에서 이미지 태그 업데이트
                        sh """
                        sed -i 's|--destination=.*|--destination=docker.io/${env.DOCKER_REPO}:${env.GIT_COMMIT_SHORT}",|' ${env.KANIKO_POD_YAML}
                        """
                    }
                }
            }
            stage('Deploy Kaniko Pod') {
                steps {
                    script {
                        // 기존 Kaniko Pod 삭제 후 새로운 Kaniko Pod 배포
                        sh """
                        kubectl delete pod ${env.KANIKO_POD_NAME} -n ${env.JENKINS_NAMESPACE} --ignore-not-found
                        kubectl create -f ${env.KANIKO_POD_YAML} -n ${env.JENKINS_NAMESPACE}
                        """
                    }
                }
            }
            stage('Wait for Kaniko Build') {
                steps {
                    script {
                        // Kaniko Pod가 완료될 때까지 대기
                        timeout(time: 15, unit: 'MINUTES') {
                            waitUntil {
                                def status = sh(script: "kubectl get pod ${env.KANIKO_POD_NAME} -n ${env.JENKINS_NAMESPACE} -o jsonpath='{.status.phase}'", returnStdout: true).trim()
                                echo "Kaniko Pod Status: ${status}"
                                return (status == 'Succeeded') || (status == 'Failed')
                            }
                        }
                        // 최종 상태 확인
                        def finalStatus = sh(script: "kubectl get pod ${env.KANIKO_POD_NAME} -n ${env.JENKINS_NAMESPACE} -o jsonpath='{.status.phase}'", returnStdout: true).trim()
                        if (finalStatus != 'Succeeded') {
                            error "Kaniko build failed with status: ${finalStatus}"
                        }
                    }
                }
            }
            stage('Deploy to Kubernetes') {
                steps {
                    script {
                        // Kubernetes에 이미지 배포
                        sh """
                        kubectl set image deployment/${env.DEPLOYMENT_NAME} \
                        -n ${env.DEPLOYMENT_NAMESPACE} ${env.DEPLOYMENT_CONTAINER_NAME}=docker.io/${env.DOCKER_REPO}:${env.GIT_COMMIT_SHORT}
                        kubectl rollout status deployment/${env.DEPLOYMENT_NAME} -n ${env.DEPLOYMENT_NAMESPACE}
                        """
                    }
                }
            }
            */
        }
        post {
            success {
                echo 'Build and push successful!'
                withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                    discordSend title: "${env.JOB_NAME} : ${env.GIT_COMMIT_MESSAGE}, ID : ${env.GIT_COMMIT_SHORT}",
                                description: "Build #${env.BUILD_NUMBER} 성공 ✅",
                                webhookURL: DISCORD
                }
            }
            failure {
                echo 'Build or deployment failed. Check logs for details.'
                withCredentials([string(credentialsId: 'Discord-Webhook', variable: 'DISCORD')]) {
                    discordSend title: "${env.JOB_NAME} : ${env.GIT_COMMIT_MESSAGE}, ID : ${env.GIT_COMMIT_SHORT}",
                                description: "Build #${env.BUILD_NUMBER} 실패 ❌",
                                webhookURL: DISCORD
                }
            }
        }
    }