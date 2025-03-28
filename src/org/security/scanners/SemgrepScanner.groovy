package org.security

class SemgrepScanner {
    static String runScan(String targetDir = '/home/kyarnk/juice-shop', String outputFile = 'semgrep_report.json') {
        return "docker run --rm -v ${targetDir}:/src -v /home/kyarnk/semgrep-reports:/output docker.io/semgrep/semgrep:latest semgrep scan --config auto --json -o /output/${outputFile}"
    }
}
