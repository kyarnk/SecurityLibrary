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
                sh '''
                    semgrep scan --config=auto --json > semgrep_report.json
                '''
            }
        }

        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'semgrep_report.json', fingerprint: true
            }
        }
    }
}

