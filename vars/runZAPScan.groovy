// def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'zap_report.json') {
//     sh """
//         docker run --rm -v /home/kyarnk/zap-reports:/zap/wrk/ -t zaproxy/zap-stable \
//         zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}
//     """
//     echo "OWASP ZAP scan completed. Report saved to /home/kyarnk/zap-reports/${outputFile}"
// }

def call(String targetUrl = '', String outputFile = 'zap_report.json', String homeDir = '') {
    if (!targetUrl || !homeDir) {
        error "Both targetUrl and homeDir must be specified!"
    }

    def outputDir = "${homeDir}/reports"
    sh "mkdir -p ${outputDir}"

    // Запуск ZAP в headless-режиме (quick scan)
    sh """
        docker run --rm -v ${outputDir}:/zap/wrk/ -t zaproxy/zap-stable \
        zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}
    """

    echo "OWASP ZAP scan completed. Report saved to ${outputDir}/${outputFile}"
}