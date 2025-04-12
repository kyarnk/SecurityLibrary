// def call(String targetDir = '', String outputFile = 'kics_report.json') {
//     sh """
//         docker run --rm -v ${targetDir}:/path checkmarx/kics:latest scan -p /path -o /path/${outputFile}
//     """
//     echo "KICS scan completed. Report saved to ${targetDir}/${outputFile}"
// }

def call(String targetDir = '', String outputFile = 'kics_report.json', String homeDir = '', String workspaceDir = '') {
    // Проверяем, если homeDir не передан, использовать пустую строку
    if (homeDir == '') {
        error "Home directory must be specified!"
    }
    
    // Если workspaceDir не указан, используем $WORKSPACE (автоматический параметр Jenkins)
    workspaceDir = workspaceDir ?: env.WORKSPACE
    
    // Директория для отчётов
    def outputDir = "${workspaceDir}/reports"

    // Создаём директорию для отчётов, если её нет
    sh "mkdir -p ${outputDir}"

    // Запуск KICS с использованием Docker
    sh """
        docker run --rm -v ${targetDir}:/path checkmarx/kics:latest scan -p /path -o /path/${outputFile}
    """
    
    echo "KICS scan completed. Report saved to ${outputDir}/${outputFile}"
}