// def call(String targetDir = env.WORKSPACE, String outputFile = 'semgrep_report.json') {
//     sh """
//         mkdir -p ${targetDir}
//         docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to ${targetDir}/${outputFile}"
// }
def call(String sourcePath = "${env.WORKSPACE}/juice-shop", String outputFile = 'semgrep_report.json') {
    def outputDir = "${env.WORKSPACE}/reports"
    sh "mkdir -p ${outputDir}"

    def command = SemgrepScanner.buildCommand(sourcePath, outputDir, outputFile)
    sh command

    echo "Semgrep scan completed. Report saved to ${outputDir}/${outputFile}"
}
