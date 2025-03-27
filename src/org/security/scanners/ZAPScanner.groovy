package org.security

class ZAPScanner {
    static String runScan(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'zap_report.json') {
        return "docker run --rm -v /home/kyarnk/zap-reports:/zap/wrk/ -t zaproxy/zap-stable " +
               "zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}"
    }
}
