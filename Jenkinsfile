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

                }
            }
        }

        stage('OWASP ZAP Scan') {
            steps {
                script {
                    runZAPScan('https://juiceshop.kyarnk.ru', 'zap_report.json')
                    
                }
            }
        }

        stage('Nuclei Scan') {
            steps {
                script {
                    runNucleiScan('https://juiceshop.kyarnk.ru', 'nuclei_report.json')
                }
            }
        }

        stage ('SCA Scan') {
            steps {
                script {
                    runSCAScan('bkimminich/juice-shop', 'syft_report.json', 'grype_report.json')
                }
            }
        }

        stage ('KICS Scan') {
            steps {
                script {
                    runKICSScan('/home/kyarnk/kics-test', 'kics_report.json')
                }
            }
        }


        stage ('Move reports') {
            steps {
                script {
                    sh 'mv /home/kyarnk/semgrep-reports/*.json $WORKSPACE/'
                    sh 'mv /home/kyarnk/zap-reports/*.json $WORKSPACE/'
                    sh 'mv /home/kyarnk/nuclei-reports/*.json $WORKSPACE/'
                    sh 'mv /home/kyarnk/sca-reports/*.json $WORKSPACE/'
                    sh 'mv /home/kyarnk/kics-test/*.json $WORKSPACE/'
                }
            }
        }

        stage('Archive Report') {
            steps {
               script {
                    archiveArtifacts artifacts: 'semgrep_report.json, zap_report.json, nuclei_report.json, syft_report.json, grype_report.json, kics_report.json', fingerprint: true
                    echo 'Reports archived.'
               }
            }
        }
    }
}



