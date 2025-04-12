package org.security

class SemgrepScanner {
    static String runScan(String targetDir = '/home/kyarnk', String outputFile = 'semgrep_report.json') {
        return """
            docker run --rm -v ${targetDir}:/mnt semgrep/semgrep semgrep --config=auto /mnt/JenkinsSecurity/juice-shop -o /mnt/reports/${outputFile}
        """
    }
}