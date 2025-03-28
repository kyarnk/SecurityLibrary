def call(String targetDir = '/home/kyarnk/juice-shop', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > /home/kyarnk/semgrep-reports/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to /home/kyarnk/semgrep-reports/${outputFile}"
}
