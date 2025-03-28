package org.security

class SemgrepScanner {
    static String runScan(String targetDir = '.', String outputDir = 'semgrep-reports', String outputFile = 'semgrep_report.json') {
        return "docker run --rm -v ${targetDir}:/src -v ${outputDir}:/output docker.io/semgrep/semgrep:latest semgrep scan --config auto --json | tee /output/${outputFile}"
    }
}