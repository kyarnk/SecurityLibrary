def call(String targetDir = '.', String outputDir = 'semgrep-reports', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v ${targetDir}:/src -v ${outputDir}:/output docker.io/semgrep/semgrep:latest semgrep scan --config auto --json | tee /output/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to ${outputDir}/${outputFile}"
}