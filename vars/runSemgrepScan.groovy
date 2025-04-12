def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v /home/kyarnk/JenkinsSecurity:/mnt semgrep/semgrep semgrep --config=auto /mnt/JenkinsSecurity/juice-shop -o /mnt/reports/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to /var/lib/jenkins/reports/${outputFile}"
}

