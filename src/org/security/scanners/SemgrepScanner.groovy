package org.security.scanners

class SemgrepScanner {
    static String buildCommand(String sourcePath, String outputFile) {
        return """
            docker run --rm -v ${sourcePath}:/src docker.io/semgrep/semgrep:latest semgrep scan --config auto --json > /src/${outputFile}
        """
    }
}