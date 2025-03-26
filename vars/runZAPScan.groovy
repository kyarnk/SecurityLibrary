def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'zap_report.json') {
    sh """
        docker run --rm -v \$(pwd):/zap/wrk/:rw -t zaproxy/zap-stable zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}
    """
    echo "OWASP ZAP scan completed. Report saved to ${outputFile}"
}
