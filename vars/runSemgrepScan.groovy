// def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
//     sh """
//         docker run --rm -v /home/kyarnk/JenkinsSecurity/juice-shop:/mnt semgrep/semgrep semgrep --config=auto /mnt -o /home/kyarnk/reports/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to /home/kyarnk/reports/${outputFile}"
// }

def call(String targetDir = '.', String outputFile = 'semgrep_report.json') {
    def tmpDir = "${pwd(tmp: true)}/semgrep-tmp"

    sh "mkdir -p ${tmpDir}"

    sh """
        docker run --rm \
            -v '${targetDir}:/mnt/source:ro' \
            -v '${tmpDir}:/mnt/output' \
            semgrep/semgrep \
            semgrep --config=auto /mnt/source -o /mnt/output/${outputFile}
    """

    sh "mv ${tmpDir}/${outputFile} ${pwd()}/${outputFile}"
    echo "Semgrep report moved to workspace."
}