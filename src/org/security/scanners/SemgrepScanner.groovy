package org.security

class SemgrepScanner {
    static String runScan(String outputFile) {
        return """
            docker run --rm -v $WORKSPACE:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > ${targetDir}/${outputFile}
        """
    }
}