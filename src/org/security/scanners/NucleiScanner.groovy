package org.security

class NucleiScanner {
    static String runScan(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'nuclei_report.json') {
        return "docker run --rm -v /home/kyarnk/nuclei-reports:/output projectdiscovery/nuclei:latest -u ${targetUrl} -o /output/${outputFile}"
    }
}