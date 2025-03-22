@Library('security-library') _

pipeline {
    agent any

        
    environment {
        // TODO: Замените на реальный URL вашего DefectDojo
        DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
        // TODO: Создайте credentials с ID 'defectdojo-api-key' в Jenkins
        DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
        // TODO: Замените на имя вашего контейнера с приложением
        CONTAINER_NAME = 'dvna'
        // ID продукта в DefectDojo
        DEFECTDOJO_PRODUCT_ID = '1'

    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Initialize DefectDojo') {
            steps {
                script {
                    // Создаем новый engagement для текущего сканирования
                    def engagement = defectdojo.createEngagement(
                        productId: env.DEFECTDOJO_PRODUCT_ID,
                        name: "Security Scan ${env.BUILD_NUMBER}",
                        branch: env.BRANCH_NAME
                    )
                    
                    // Сохраняем ID engagement для использования в следующих этапах
                    env.DEFECTDOJO_ENGAGEMENT_ID = engagement.id.toString()
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    // Запуск Semgrep внутри контейнера
                    runSemgrepScan(
                        containerName: env.CONTAINER_NAME,
                        scanPath: '/var/www/html',  // Путь к файлам внутри контейнера
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск KICS внутри контейнера
                    runKICSScan(
                        containerName: env.CONTAINER_NAME,
                        scanPath: '/var/www/html',  // Путь к файлам внутри контейнера
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск Nuclei (динамическое сканирование)
                    // TODO: Замените порт на тот, который использует ваше приложение
                    runNucleiScan(
                        targetUrl: 'http://localhost:9090',
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск OWASP ZAP (динамическое сканирование)
                    // TODO: Замените порт на тот, который использует ваше приложение
                    runOWASPZAPScan(
                        targetUrl: 'http://localhost:9090',
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск Gitleaks для поиска секретов в коде
                    runGitleaksScan(
                        scanPath: '.',
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск Syft для анализа состава ПО
                    // TODO: Замените на имя и тег вашего Docker образа
                    runSyftScan(
                        imageName: 'dvna:latest',
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Запуск Grype для поиска уязвимостей в зависимостях
                    // TODO: Замените на имя и тег вашего Docker образа
                    runGrypeScan(
                        imageName: 'dvna:latest',
                        defectDojoUrl: env.DEFECTDOJO_URL,
                        defectDojoApiKey: env.DEFECTDOJO_API_KEY,
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                }
            }
        }
        
        stage('Report') {
            steps {
                script {
                    // Получаем все находки для текущего engagement
                    def findings = defectdojo.searchFindings(
                        engagementId: env.DEFECTDOJO_ENGAGEMENT_ID
                    )
                    
                    // Выводим статистику
                    echo "Найдено уязвимостей: ${findings.count}"
                    echo "Ссылка на отчет: ${env.DEFECTDOJO_URL}/engagement/${env.DEFECTDOJO_ENGAGEMENT_ID}"
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}