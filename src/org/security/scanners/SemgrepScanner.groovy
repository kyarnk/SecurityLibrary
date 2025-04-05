package org.security.scanners

class SemgrepScanner {
    static String buildCommand(String sourcePath, String outputDir, String outputFile) {
        return """
            docker run --rm \
              -v ${sourcePath}:/target \
              -v ${outputDir}:/src \
              docker.io/semgrep/semgrep:latest \
              semgrep scan --config auto --json --quiet --error --output /src/${outputFile} /target
        """.stripIndent().trim()
    }
}