def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    sh """
        docker run --rm -v $HOME:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > $HOME/reports/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to $HOME/reports/${outputFile}"
}
