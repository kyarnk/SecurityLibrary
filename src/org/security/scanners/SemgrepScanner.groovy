package org.security

class SemgrepScanner {
    static String runScan(String targetDir, String outputFile) {
        return """
            mkdir -p ${targetDir}
            docker run --rm -v ${targetDir}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
        """
    }
}