def call(String targetDir = '/home/kyarnk/juice-shop', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v ${targetDir}:/src -v /home/kyarnk/semgrep-reports:/output docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > /output/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to /home/kyarnk/semgrep-reports/${outputFile}"
}