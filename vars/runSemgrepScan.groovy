// def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
//     sh """
//         docker run --rm -v /home/kyarnk/JenkinsSecurity/juice-shop:/mnt semgrep/semgrep semgrep --config=auto /mnt -o /home/kyarnk/reports/${outputFile}
//     """
//     echo "Semgrep scan completed. Report saved to /home/kyarnk/reports/${outputFile}"
// }


// def call(String targetDir = '.', String outputFile = 'semgrep_report.json') {
//     def tmpDir = "${pwd(tmp: true)}/semgrep-tmp"

//     sh "mkdir -p ${tmpDir}"

//     sh """
//         docker run --rm \
//             -v '${targetDir}:/mnt/source:ro' \
//             -v '${tmpDir}:/mnt/output' \
//             semgrep/semgrep \
//             semgrep --config=auto /mnt/source -o /mnt/output/${outputFile}
//     """

//     sh "mv ${tmpDir}/${outputFile} ${pwd()}/${outputFile}"
//     echo "Semgrep report moved to workspace."
// }

def call(String targetDir = '', String outputFile = 'semgrep_report.json') {
    def containerName = "semgrep-${UUID.randomUUID().toString()}"
    
    // Создаём временные директории
    sh "mkdir -p semgrep_output"

    // Запускаем контейнер в фоне
    sh """
        docker run -d --name ${containerName} \
        -v \$WORKSPACE/${targetDir}:/mnt/source:ro \
        -v \$WORKSPACE/semgrep_output:/mnt/output \
        semgrep/semgrep tail -f /dev/null
    """

    // Запускаем саму проверку
    sh """
        docker exec ${containerName} semgrep --config=auto /mnt/source -o /mnt/output/${outputFile}
    """

    // Копируем результат обратно (он уже в томе, можно просто обращаться к WORKSPACE)
    sh """
        cp semgrep_output/${outputFile} ${outputFile}
    """

    // Удаляем контейнер
    sh "docker rm -f ${containerName}"

    echo "Semgrep scan completed. Report saved to \$WORKSPACE/${outputFile}"
}
