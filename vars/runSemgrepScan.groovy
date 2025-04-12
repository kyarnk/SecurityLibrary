// def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
//     sh """
//         docker run --rm -v /var/lib/jenkins:/mnt semgrep/semgrep semgrep --config=auto /mnt/JenkinsSecurity/juice-shop -o /mnt/reports/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to /var/lib/jenkins/reports/${outputFile}"
// }

def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    def scanPath = targetDir?.trim() ? targetDir : env.WORKSPACE
    def containerTarget = "/mnt/target"
    def containerReports = "/mnt/reports"

    sh """
        mkdir -p ${env.WORKSPACE}/reports
        docker run --rm \
            -v ${scanPath}:${containerTarget} \
            -v ${env.WORKSPACE}/reports:${containerReports} \
            semgrep/semgrep \
            semgrep --config=auto ${containerTarget} -o ${containerReports}/${outputFile}
    """
    echo "Semgrep scan completed. Report saved to ${env.WORKSPACE}/reports/${outputFile}"
}