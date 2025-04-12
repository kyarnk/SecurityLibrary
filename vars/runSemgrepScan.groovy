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

def call(String targetDir = '.', String outputFile = 'semgrep_report.json') {
    def containerName = "semgrep_${UUID.randomUUID().toString().replaceAll('-', '')}"
    def containerSourcePath = "/mnt/source"
    def containerOutputPath = "/mnt/output"

    // Создаём контейнер с нужной командой
    sh """
        docker create --name ${containerName} semgrep/semgrep semgrep --config=auto ${containerSourcePath} -o ${containerOutputPath}/${outputFile}
    """

    // Копируем исходный код внутрь контейнера
    sh "docker cp '${targetDir}' ${containerName}:${containerSourcePath}"

    // Создаём папку для отчёта
    sh "docker exec ${containerName} mkdir -p ${containerOutputPath}"

    // Запускаем сканирование
    sh "docker start -a ${containerName}"

    // Копируем отчёт обратно
    sh "docker cp ${containerName}:${containerOutputPath}/${outputFile} ${outputFile}"

    // Удаляем контейнер
    sh "docker rm ${containerName}"

    echo "Semgrep scan completed. Report saved to: ${outputFile}"
}