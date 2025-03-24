
def call(String targetDir = '.', String outputFile = 'semgrep_report.json') {
    def semgrepCommand = "docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${outputFile}"
    
    sh "${semgrepCommand}"
    
    echo "Semgrep scan completed. Report saved to ${outputFile}"
}

 