# Jenkins Security Library

Библиотека для Jenkins, предоставляющая набор инструментов для автоматизации проверок безопасности в процессе CI/CD. Библиотека интегрирует популярные инструменты безопасности и автоматически отправляет результаты в DefectDojo для централизованного управления уязвимостями.

## Содержание

1. [Возможности](#возможности)
2. [Поддерживаемые инструменты](#поддерживаемые-инструменты)
3. [Требования](#требования)
4. [Установка](#установка)
5. [Конфигурация](#конфигурация)
6. [Использование](#использование)
7. [API Reference](#api-reference)
8. [Примеры](#примеры)
9. [Устранение неполадок](#устранение-неполадок)
10. [Разработка](#разработка)

## Возможности

- Автоматизация запуска инструментов безопасности
- Интеграция с DefectDojo для управления уязвимостями
- Поддержка контейнеризированных приложений
- Гибкая конфигурация для каждого сканера
- Единый интерфейс для всех инструментов
- Автоматическая генерация отчетов
- Поддержка параллельного выполнения сканирований

## Поддерживаемые инструменты

### Статический анализ (SAST)
- **Semgrep**
  - Анализ исходного кода
  - Поиск уязвимостей безопасности
  - Проверка качества кода
  - Поддерживаемые языки: Python, JavaScript, Java, Go и другие

### Анализ конфигураций (IaC)
- **KICS**
  - Проверка конфигураций инфраструктуры
  - Поддержка Terraform, Kubernetes, Docker, CloudFormation
  - Выявление проблем безопасности в IaC

### Динамический анализ (DAST)
- **Nuclei**
  - Сканирование веб-приложений
  - Обнаружение уязвимостей в реальном времени
  - Расширяемый набор шаблонов

- **OWASP ZAP**
  - Полнофункциональный DAST-сканер
  - Активное и пассивное сканирование
  - API-сканирование

### Анализ секретов
- **Gitleaks**
  - Поиск секретов в Git-репозиториях
  - Предотвращение утечки чувствительных данных
  - Настраиваемые правила поиска

### Анализ контейнеров
- **Syft**
  - Создание SBOM (Software Bill of Materials)
  - Анализ зависимостей в контейнерах
  - Поддержка различных форматов

- **Grype**
  - Сканирование уязвимостей в контейнерах
  - Проверка зависимостей
  - Интеграция с базами CVE

## Требования

### Системные требования
- Jenkins 2.375.1 или выше
- Docker 20.10 или выше
- Python 3.8 или выше
- Go 1.19 или выше
- Java 11 или выше
- curl
- git

### Сетевые требования
- Доступ к Docker Hub
- Доступ к GitHub
- Доступ к DefectDojo
- Доступ к PyPI (для Python-зависимостей)

### Права доступа
- Права на установку Jenkins plugins
- Доступ к Docker daemon
- Права на создание/удаление контейнеров
- Доступ к API DefectDojo

## Установка

### 1. Добавление библиотеки в Jenkins

1. Перейдите в Jenkins > Manage Jenkins > Configure System
2. Найдите секцию "Global Pipeline Libraries"
3. Нажмите "Add"
4. Заполните поля:
   ```
   Name: security-library
   Default Version: main
   Retrieval method: Modern SCM
   Source Code Management: Git
   Project Repository: URL вашего репозитория
   Credentials: Добавьте при необходимости
   ```

### 2. Установка зависимостей

```bash
# Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Python и pip
sudo apt update
sudo apt install python3 python3-pip

# Go (для Nuclei)
wget https://go.dev/dl/go1.19.linux-amd64.tar.gz
sudo tar -C /usr/local -xzf go1.19.linux-amd64.tar.gz
export PATH=$PATH:/usr/local/go/bin

# Java
sudo apt install openjdk-11-jdk
```

### 3. Настройка прав доступа

```bash
# Добавление пользователя Jenkins в группу docker
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

## Конфигурация

### DefectDojo

1. Создание API ключа:
   - Войдите в DefectDojo
   - Перейдите в API v2 > Credentials
   - Создайте новый API ключ

2. Добавление ключа в Jenkins:
   - Jenkins > Credentials > System > Global credentials
   - Add Credentials
   - Kind: Secret text
   - Secret: Ваш API ключ
   - ID: defectdojo-api-key

3. Настройка переменных окружения:
   ```groovy
   environment {
       DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
       DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
   }
   ```

### Настройка сканеров

#### Semgrep
```groovy
runSemgrepScan(
    containerName: 'app',           // Имя контейнера с приложением
    scanPath: '/app',              // Путь к коду внутри контейнера
    rules: ['p/owasp-top-ten'],    // Набор правил
    defectDojoUrl: env.DEFECTDOJO_URL,
    defectDojoApiKey: env.DEFECTDOJO_API_KEY,
    engagementId: '1'              // ID в DefectDojo
)
```

#### KICS
```groovy
runKICSScan(
    containerName: 'infra',
    scanPath: '/terraform',
    platforms: ['terraform', 'k8s'],
    defectDojoUrl: env.DEFECTDOJO_URL,
    defectDojoApiKey: env.DEFECTDOJO_API_KEY,
    engagementId: '1'
)
```

#### Nuclei
```groovy
runNucleiScan(
    targetUrl: 'http://app:8080',
    templates: ['cves', 'vulnerabilities'],
    defectDojoUrl: env.DEFECTDOJO_URL,
    defectDojoApiKey: env.DEFECTDOJO_API_KEY,
    engagementId: '1'
)
```

## Использование

### Базовое использование библиотеки

1. Добавьте библиотеку в ваш Jenkinsfile:
   ```groovy
   @Library('security-library') _
   ```

2. Настройте переменные окружения:
   ```groovy
   environment {
       DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
       DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
   }
   ```

3. Используйте доступные функции сканирования в pipeline:

   ```groovy
   stage('Security Scan') {
       steps {
           script {
               // SAST сканирование
               runSemgrepScan(
                   containerName: 'app',
                   scanPath: '/app',
                   engagementId: '1'
               )

               // Сканирование инфраструктуры
               runKICSScan(
                   containerName: 'infra',
                   scanPath: '/terraform',
                   engagementId: '1'
               )

               // DAST сканирование
               runNucleiScan(
                   targetUrl: 'http://app:8080',
                   engagementId: '1'
               )
           }
       }
   }
   ```

### Использование Pipeline Script from SCM

Альтернативный способ использования библиотеки - через Pipeline Script from SCM:

1. Создайте файл `Jenkinsfile` в корне вашего репозитория:
   ```groovy
   @Library('security-library') _
   
   pipeline {
       agent any
       
       environment {
           DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
           DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
       }
       
       stages {
           stage('Security Scan') {
               steps {
                   script {
                       // Ваши шаги сканирования
                   }
               }
           }
       }
   }
   ```

2. Настройте pipeline в Jenkins:
   - Создайте новый Pipeline job
   - В секции "Pipeline" выберите "Pipeline script from SCM"
   - Укажите:
     - SCM: Git
     - Repository URL: URL вашего репозитория
     - Credentials: если требуются
     - Branch Specifier: */main (или вашу ветку)
     - Script Path: Jenkinsfile

3. Преимущества этого подхода:
   - Jenkinsfile хранится в системе контроля версий
   - История изменений pipeline отслеживается вместе с кодом
   - Возможность code review для изменений в pipeline
   - Автоматический запуск при изменениях в репозитории

### Параллельное выполнение

Для оптимизации времени выполнения можно запускать сканеры параллельно:

```groovy
stage('Security Scan') {
    steps {
        script {
            parallel(
                "SAST": {
                    runSemgrepScan(...)
                },
                "IaC": {
                    runKICSScan(...)
                },
                "DAST": {
                    runNucleiScan(...)
                }
            )
        }
    }
}
```

### Управление результатами

1. Создание нового engagement:
   ```groovy
   def engagement = defectdojo.createEngagement(
       productId: '1',
       name: "Security Scan ${BUILD_NUMBER}"
   )
   ```

2. Получение результатов:
   ```groovy
   def findings = defectdojo.searchFindings(
       engagementId: engagement.id
   )
   ```

3. Обработка результатов:
   ```groovy
   if (findings.findAll { it.severity in ['Critical', 'High'] }.size() > 0) {
       error "Обнаружены критические уязвимости!"
   }
   ```

### Настройка уведомлений

Добавьте обработку результатов в блок `post`:

```groovy
post {
    always {
        script {
            def findings = defectdojo.searchFindings(
                engagementId: env.ENGAGEMENT_ID
            )
            
            echo """
            Результаты сканирования:
            - Всего уязвимостей: ${findings.count}
            - Критических: ${findings.findAll { it.severity == 'Critical' }.size()}
            - Высоких: ${findings.findAll { it.severity == 'High' }.size()}
            """
        }
    }
}
```

### Лучшие практики

1. **Версионирование**: Всегда указывайте конкретную версию библиотеки:
   ```groovy
   @Library('security-library@v1.0.0') _
   ```

2. **Обработка ошибок**: Используйте блоки try-catch для обработки ошибок сканеров:
   ```groovy
   try {
       runSemgrepScan(...)
   } catch (Exception e) {
       echo "Ошибка при SAST сканировании: ${e.message}"
       currentBuild.result = 'UNSTABLE'
   }
   ```

3. **Кэширование**: Используйте кэширование Docker образов для ускорения сканирования:
   ```groovy
   options {
       skipDefaultCheckout()
   }
   ```

4. **Документирование**: Добавляйте комментарии к настройкам сканеров:
   ```groovy
   runKICSScan(
       containerName: 'infra',
       scanPath: '/terraform',
       // Указываем только нужные платформы для сканирования
       platforms: ['terraform', 'k8s'],
       engagementId: '1'
   )
   ```

## API Reference

### DefectDojo API

#### Создание engagement
```groovy
defectdojo.createEngagement(
    productId: '1',
    name: 'Security Scan',
    branch: 'main'
)
```

#### Загрузка результатов
```groovy
defectdojo.uploadScanResults(
    engagementId: '1',
    reportFile: 'results.json',
    scanType: 'Semgrep Scan'
)
```

#### Поиск уязвимостей
```groovy
defectdojo.searchFindings(
    engagementId: '1'
)
```

## Примеры

### Базовый pipeline
```groovy
@Library('security-library') _

pipeline {
    agent any
    
    environment {
        DEFECTDOJO_URL = 'https://defectdojo.company.com'
        DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
        CONTAINER_NAME = 'app'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    // Создаем engagement
                    def engagement = defectdojo.createEngagement(
                        productId: '1',
                        name: "Security Scan ${BUILD_NUMBER}"
                    )
                    
                    // Запускаем сканеры
                    parallel(
                        "SAST": {
                            runSemgrepScan(
                                containerName: env.CONTAINER_NAME,
                                scanPath: '/app',
                                engagementId: engagement.id
                            )
                        },
                        "DAST": {
                            runNucleiScan(
                                targetUrl: 'http://app:8080',
                                engagementId: engagement.id
                            )
                        },
                        "Secrets": {
                            runGitleaksScan(
                                scanPath: '.',
                                engagementId: engagement.id
                            )
                        }
                    )
                }
            }
        }
    }
}
```

### Полный pipeline с отчетами
```groovy
@Library('security-library') _

pipeline {
    agent any
    
    environment {
        DEFECTDOJO_URL = 'https://defectdojo.company.com'
        DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
        CONTAINER_NAME = 'app'
        PRODUCT_ID = '1'
    }
    
    stages {
        stage('Initialize') {
            steps {
                script {
                    // Проверяем доступность DefectDojo
                    def product = defectdojo.getProduct(productId: env.PRODUCT_ID)
                    echo "Сканирование для продукта: ${product.name}"
                    
                    // Создаем engagement
                    def engagement = defectdojo.createEngagement(
                        productId: env.PRODUCT_ID,
                        name: "Security Scan ${BUILD_NUMBER}",
                        branch: env.BRANCH_NAME
                    )
                    env.ENGAGEMENT_ID = engagement.id.toString()
                }
            }
        }
        
        stage('Security Scan') {
            parallel {
                stage('SAST') {
                    steps {
                        runSemgrepScan(
                            containerName: env.CONTAINER_NAME,
                            scanPath: '/app',
                            engagementId: env.ENGAGEMENT_ID
                        )
                    }
                }
                
                stage('IaC') {
                    steps {
                        runKICSScan(
                            containerName: env.CONTAINER_NAME,
                            scanPath: '/infra',
                            engagementId: env.ENGAGEMENT_ID
                        )
                    }
                }
                
                stage('DAST') {
                    steps {
                        runNucleiScan(
                            targetUrl: 'http://app:8080',
                            engagementId: env.ENGAGEMENT_ID
                        )
                        
                        runOWASPZAPScan(
                            targetUrl: 'http://app:8080',
                            engagementId: env.ENGAGEMENT_ID
                        )
                    }
                }
                
                stage('Container') {
                    steps {
                        runSyftScan(
                            imageName: "${env.CONTAINER_NAME}:latest",
                            engagementId: env.ENGAGEMENT_ID
                        )
                        
                        runGrypeScan(
                            imageName: "${env.CONTAINER_NAME}:latest",
                            engagementId: env.ENGAGEMENT_ID
                        )
                    }
                }
            }
        }
        
        stage('Report') {
            steps {
                script {
                    def findings = defectdojo.searchFindings(
                        engagementId: env.ENGAGEMENT_ID
                    )
                    
                    echo """
                    Результаты сканирования:
                    - Всего уязвимостей: ${findings.count}
                    - Критических: ${findings.findAll { it.severity == 'Critical' }.size()}
                    - Высоких: ${findings.findAll { it.severity == 'High' }.size()}
                    - Средних: ${findings.findAll { it.severity == 'Medium' }.size()}
                    - Низких: ${findings.findAll { it.severity == 'Low' }.size()}
                    
                    Отчет доступен по ссылке: ${env.DEFECTDOJO_URL}/engagement/${env.ENGAGEMENT_ID}
                    """
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        failure {
            script {
                def findings = defectdojo.searchFindings(
                    engagementId: env.ENGAGEMENT_ID
                )
                if (findings.findAll { it.severity in ['Critical', 'High'] }.size() > 0) {
                    error "Обнаружены критические уязвимости!"
                }
            }
        }
    }
}
```

## Устранение неполадок

### Общие проблемы

1. **Ошибка подключения к DefectDojo**
   ```
   Проблема: Ошибка при запросе к DefectDojo API
   Решение:
   - Проверьте URL в DEFECTDOJO_URL
   - Убедитесь, что API ключ действителен
   - Проверьте сетевую доступность DefectDojo
   ```

2. **Ошибки Docker**
   ```
   Проблема: Cannot connect to the Docker daemon
   Решение:
   - Проверьте права пользователя Jenkins
   - Перезапустите Docker daemon
   - Проверьте статус Docker: systemctl status docker
   ```

3. **Ошибки сканеров**
   ```
   Проблема: Сканер не может получить доступ к файлам
   Решение:
   - Проверьте пути монтирования в Docker
   - Убедитесь, что файлы доступны внутри контейнера
   - Проверьте права доступа к файлам
   ```
4. **Ошибка при заходе в DefectDojo повторно**
   ```
   Проблема: В базе уже имеются данные админа
   Решение:
   - Создать нового пользователя
   docker-compose exec uwsgi /bin/bash -c 'python manage.py createsuperuser'
   ```

## Разработка

### Структура проекта
```
.
├── src/
│   └── org/
│       └── security/
│           └── scanners/           # Классы сканеров
│               ├── BaseScanner.groovy
│               ├── SemgrepScanner.groovy
│               ├── KICSScanner.groovy
│               ├── NucleiScanner.groovy
│               ├── OWASPZAPScanner.groovy
│               ├── GitleaksScanner.groovy
│               ├── SyftScanner.groovy
│               ├── GrypeScanner.groovy
│               └── DefectDojoAPI.groovy
└── vars/                          # Глобальные функции
    ├── runSemgrepScan.groovy
    ├── runKICSScan.groovy
    ├── runNucleiScan.groovy
    ├── runOWASPZAPScan.groovy
    ├── runGitleaksScan.groovy
    ├── runSyftScan.groovy
    ├── runGrypeScan.groovy
    └── defectdojo.groovy
```

### Добавление нового сканера

1. Создайте класс сканера в `src/org/security/scanners/`:
   ```groovy
   package org.security.scanners

   class NewScanner extends BaseScanner {
       NewScanner(script, config) {
           super(script, config)
       }
       
       def run() {
           // Реализация сканирования
       }
       
       def parseResults() {
           // Парсинг результатов
       }
   }
   ```

2. Создайте глобальную функцию в `vars/`:
   ```groovy
   def call(Map config) {
       def scanner = new NewScanner(this, config)
       scanner.run()
   }
   ```
