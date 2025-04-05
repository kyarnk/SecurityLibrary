def call(String imageName = 'bkimminich/juice-shop', String syftOutput = 'syft_report.json', String grypeOutput = 'grype_report.json') {
    sh """
        syft ${imageName} -o json > $HOME/sca-reports/${syftOutput}
        grype sbom:$HOME/sca-reports/${syftOutput} -o json > $HOME/sca-reports/${grypeOutput}
    """
    echo "SCA scan completed. Reports saved to $HOME/sca-reports/"
}
