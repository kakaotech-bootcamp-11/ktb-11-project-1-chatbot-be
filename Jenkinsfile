pipeline {
  agent any
    stages('git scm update') {
      steps {
        git url: "https://github.com/kakaotech-bootcamp-11/ktb-11-project-1-chatbot-be.git", branch: 'feature/jenkins'
        }
      }
    stage('deploy kubernetes') {
      steps {
      sh '''
      kubectl apply deployment backend-deployment --image=ktb11chatbot/ktb-11-project-1-chatbot-be:latest
      '''
      }
    }
  }
}