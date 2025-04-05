package org.security

class SemgrepScanner {
    static String runScan(String targetDir = '', String outputFile = 'semgrep_report.json') {
        return """
            docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
        """
    }
}