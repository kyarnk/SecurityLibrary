package org.security

class SCAScanner {
    static String runSyftScan(String imageName = 'bkimminich/juice-shop', String outputFile = 'syft_report.json') {
        return "syft ${imageName} -o json > /home/kyarnk/sca-reports/${outputFile}"
    }

    static String runGrypeScan(String sbomFile = '/home/kyarnk/sca-reports/syft_report.json', String outputFile = 'grype_report.json') {
        return "grype sbom:${sbomFile} -o json > /home/kyarnk/sca-reports/${outputFile}"
    }
}
