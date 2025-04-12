// def call(String imageName = 'bkimminich/juice-shop', String syftOutput = 'syft_report.json', String grypeOutput = 'grype_report.json') {
//     sh """
//         syft ${imageName} -o json > $HOME/sca-reports/${syftOutput}
//         grype sbom:$HOME/sca-reports/${syftOutput} -o json > $HOME/sca-reports/${grypeOutput}
//     """
//     echo "SCA scan completed. Reports saved to $HOME/sca-reports/"
// }

def call(String imageName = '', String syftOutputFile = 'syft_report.json', String grypeOutputFile = 'grype_report.json', String homeDir = '', String workspaceDir = '') {
    // Проверяем, если homeDir не передан, использовать пустую строку
    if (homeDir == '') {
        error "Home directory must be specified!"
    }

    // Если workspaceDir не указан, используем $WORKSPACE (автоматический параметр Jenkins)
    workspaceDir = workspaceDir ?: env.WORKSPACE

    // Директория для отчётов
    def outputDir = "${homeDir}/reports"

    // Создаём директорию для отчётов, если её нет
    sh "mkdir -p ${outputDir}"

    // Запуск Syft с переданными параметрами
    sh """
        syft ${imageName} -o json > ${outputDir}/${syftOutputFile}
    """
    echo "Syft scan completed. Report saved to ${outputDir}/${syftOutputFile}"

    // Запуск Grype с переданными параметрами
    sh """
        grype sbom:${outputDir}/${syftOutputFile} -o json > ${outputDir}/${grypeOutputFile}
    """
    echo "Grype scan completed. Report saved to ${outputDir}/${grypeOutputFile}"
}
