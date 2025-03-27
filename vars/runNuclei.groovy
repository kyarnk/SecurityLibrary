def call(String targetUrl = 'https://juiceshop.kyarnk.ru', String outputFile = 'nuclei_report.json') {
    sh """
        docker run --rm -v /home/kyarnk/nuclei-reports:/output projectdiscovery/nuclei:latest -u ${targetUrl} -o /output/${outputFile}
    """
    echo "Nuclei scan completed. Report saved to /home/kyarnk/nuclei-reports/${outputFile}"
}