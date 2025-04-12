def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v /home/kyarnk/JenkinsSecurity/juice-shop:/mnt semgrep/semgrep semgrep --config=auto /mnt -o /mnt/reports/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to /home/kyarnk/reports/${outputFile}"
}

