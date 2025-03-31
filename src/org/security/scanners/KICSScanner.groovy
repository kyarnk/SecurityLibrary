package org.security

class KICSScanner {
    static String runScan(String targetDir = '/home/kyarnk/kics-test', String outputFile = 'kics_report.json') {
        return "docker run --rm -v ${targetDir}:/path checkmarx/kics:latest scan -p /path -o /path/${outputFile}"
    }
}
