def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'zap_report.json') {
    sh """
        docker run --rm -v /home/kyarnk/zap-reports:/zap/wrk/ -t zaproxy/zap-stable \
        zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}
    """
    echo "OWASP ZAP scan completed. Report saved to /home/kyarnk/zap-reports/${outputFile}"
}
