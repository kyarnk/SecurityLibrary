def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > /home/kyarnk/semgrep-reports/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to /home/kyarnk/semgrep-reports/${outputFile}"
}
