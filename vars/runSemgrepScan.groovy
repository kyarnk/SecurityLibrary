def call(String targetDir = env.WORKSPACE, String outputFile = 'semgrep_report.json') {
    sh """
        mkdir -p ${targetDir}
        docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to ${targetDir}/${outputFile}"
}