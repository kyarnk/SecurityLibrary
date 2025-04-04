def call(String targetDir = '', String outputFile = 'kics_report.json') {
    sh """
        docker run --rm -v ${targetDir}:/path checkmarx/kics:latest scan -p /path -o /path/${outputFile}
    """
    echo "KICS scan completed. Report saved to ${targetDir}/${outputFile}"
}
