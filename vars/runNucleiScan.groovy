// def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'nuclei_report.json') {
//     sh """
//         docker run --rm -v /home/kyarnk/nuclei-reports:/output projectdiscovery/nuclei:latest -u ${targetUrl} -o /output/${outputFile}
//     """
//     echo "Nuclei scan completed. Report saved to /home/kyarnk/nuclei-reports/${outputFile}"
// }

def call(String targetUrl = '', String outputFile = 'nuclei_report.json', String homeDir = '') {
    if (!targetUrl || !homeDir) {
        error "Both targetUrl and homeDir must be specified!"
    }

    def outputDir = "${homeDir}/reports"
    sh "mkdir -p ${outputDir}"

    // Запуск Nuclei
    sh """
        docker run --rm -v ${outputDir}:/output projectdiscovery/nuclei:latest \
        -u ${targetUrl} -o /output/${outputFile} -j
    """

    echo "Nuclei scan completed. Report saved to ${outputDir}/${outputFile}"
}