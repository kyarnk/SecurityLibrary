@Library('security-library') _

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
                script {
                    runSemgrepScan('/home/kyarnk/juice-shop', 'semgrep_report.json')

                    // Выводим результат в консоль
                    def report = readFile('semgrep_report.json')
                    echo "Semgrep Scan Results: \n${report}"
                }
            }
        }

        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'semgrep_report.json', fingerprint: true
            }
        }
    }
}



