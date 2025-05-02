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


## Возможности

- Автоматизация запуска инструментов безопасности
- Интеграция с DefectDojo для управления уязвимостями
- Поддержка контейнеризированных приложений
- Гибкая конфигурация для каждого сканера
- Единый интерфейс для всех инструментов
- Автоматическая генерация отчетов

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
 
### Анализ контейнеров
- **Syft**
  - Создание SBOM (Software Bill of Materials)
  - Анализ зависимостей в контейнерах
  - Поддержка различных форматов

- **Grype**
  - Сканирование уязвимостей в контейнерах
  - Проверка зависимостей
  - Интеграция с базами CVE

### Динамический анализ (DAST)
- **Nuclei**
  - Сканирование веб-приложений
  - Обнаружение уязвимостей в реальном времени
  - Расширяемый набор шаблонов

- **OWASP ZAP**
  - Полнофункциональный DAST-сканер
  - Активное и пассивное сканирование
  - API-сканирование

## Требования

### Системные требования
- Jenkins 2.492.2 или выше
- Docker 20.10 или выше
- Python 3.8 или выше
- Go 1.19 или выше
- Java 17 или выше (после 31 марта Java 21)
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
Рекомендуется установка по официальной документации
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Python и pip
sudo apt update
sudo apt install python3 python3-pip

# Go (для Nuclei)
Рекомендуется установка по официальной документации

#Nuclei Github
git clone https://github.com/projectdiscovery/nuclei.git; \
cd nuclei/cmd/nuclei; \
go build; \
mv nuclei /usr/local/bin/; \
nuclei -version;

# Java
sudo apt install openjdk-17-jdk
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
```

```

#### KICS
```

```
#### Syft and Grype
```

```

#### Nuclei
```

```
#### OWASP ZAP
```

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

Альтернативный способ использования библиотеки - это Pipeline Script from SCM:

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

