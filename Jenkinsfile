pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Semgrep Scan') {
            steps {
                runSemgrepScan('.', 'semgrep_report.json')  // Используем библиотеку
            }
        }

        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'semgrep_report.json', fingerprint: true
            }
        }
    }
}


