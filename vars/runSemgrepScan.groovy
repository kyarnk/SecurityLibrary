// def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
//     sh """
//         docker run --rm -v /var/lib/jenkins:/mnt semgrep/semgrep semgrep --config=auto /mnt/JenkinsSecurity/juice-shop -o /mnt/reports/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to /var/lib/jenkins/reports/${outputFile}"
// }

def call(String scanPath = '.', String outputFile = 'semgrep_report.json') {
    def reportDir = 'reports'
    sh """
        mkdir -p ${reportDir}
        semgrep --config=auto ${scanPath} -o ${reportDir}/${outputFile}
    """
    echo "✅ Semgrep scan completed. Report saved to ${reportDir}/${outputFile}"
}