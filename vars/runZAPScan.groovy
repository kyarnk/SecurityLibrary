def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'zap_report.json') {
    sh """
        docker run --rm -v /tmp:/zap/wrk/ -t zaproxy/zap-stable zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/zap_temp.json
        cp /tmp/zap_temp.json ${outputFile}
    """
    echo "OWASP ZAP scan completed. Report saved to ${outputFile}"
}
