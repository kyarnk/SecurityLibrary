def call(Map config) {
    def image = docker.build("defectdojo-uploader", "${libraryResource('scripts/uploader/Dockerfile')}")

    image.inside {
        writeFile file: 'report.json', text: readFile(config.file)

        sh """
            python upload_reports.py \
              --token '${env.DEFECTDOJO_TOKEN}' \
              --host '${env.DEFECTDOJO_HOST}' \
              --file 'report.json' \
              --engagement '${config.engagement}' \
              --scan_type '${config.scanType}' \
              --scan_date '${config.scanDate}'
        """
    }
}
