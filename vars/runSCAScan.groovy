def call(String imageName = 'bkimminich/juice-shop', String syftOutput = 'syft_report.json', String grypeOutput = 'grype_report.json') {
    sh """
        syft ${imageName} -o json > /home/kyarnk/sca-reports/${syftOutput}
        grype sbom:/home/kyarnk/sca-reports/${syftOutput} -o json > /home/kyarnk/sca-reports/${grypeOutput}
    """
    echo "SCA scan completed. Reports saved to /home/kyarnk/sca-reports/"
}
