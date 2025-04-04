package org.security

class SCAScanner {
    static String runSyftScan(String imageName = 'bkimminich/juice-shop', String outputFile = 'syft_report.json') {
        return "syft ${imageName} -o json > $HOME/sca-reports/${outputFile}"
    }

    static String runGrypeScan(String sbomFile = '$HOME/sca-reports/syft_report.json', String outputFile = 'grype_report.json') {
        return "grype sbom:${sbomFile} -o json > $HOME/sca-reports/${outputFile}"
    }
}
