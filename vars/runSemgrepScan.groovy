// def call(String targetDir = env.WORKSPACE, String outputFile = 'semgrep_report.json') {
//     sh """
//         mkdir -p ${targetDir}
//         docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to ${targetDir}/${outputFile}"
// }
import org.security.scanners.SemgrepScanner

def call(String sourcePath = "${env.WORKSPACE}", String outputFile = 'semgrep_report.json') {
    def fullSourcePath = sourcePath.startsWith("/") ? sourcePath : "${env.WORKSPACE}/${sourcePath}"
    def command = SemgrepScanner.buildCommand(fullSourcePath, outputFile)

    sh command
    echo "Semgrep scan completed. Report saved to ${fullSourcePath}/${outputFile}"
}
