# 🔐 Jenkins Security Library

Библиотека Groovy-скриптов для Jenkins, обеспечивающая автоматизацию запусков инструментов анализа безопасности (SAST, DAST, IaC, SCA) и централизованную отправку результатов в DefectDojo.

## Содержание

- Возможности  
- Поддерживаемые инструменты  
- Требования  
- Установка  
- Конфигурация  
- Использование    
- Структура 

## Возможности

- Автоматизация запуска инструментов безопасности
- Автоматическая генерация отчетов в Jenkins при использовании Archive
- Поддержка Docker-контейнеров и локальных путей  
- Интеграция с DefectDojo: создание engagements, загрузка результатов, контроль уязвимостей  
- Параллельное выполнение сканирований  
- Гибкая настройка через переменные окружения  
- Расширяемая архитектура (удобное добавление новых инструментов)  

## Поддерживаемые инструменты

### Статический анализ (SAST)

- **Semgrep**  
  - Проверка исходного кода (Python, JS, Go, Java и др.)  
  - Поддержка кастомных правил
  - Проверка качества кода

###  Анализ инфраструктуры как кода (IaC)

- **KICS**  
  - Анализ Terraform, Kubernetes YAML, Dockerfile и CloudFormation 
  - Выявление проблем безопасности в IaC

### Анализ контейнеров (SCA)

- **Syft**  
  - Генерация SBOM 
  - Анализ зависимостей в контейнерах
- **Grype**  
  - Проверка зависимостей на уязвимости по CVE 

###  Динамический анализ (DAST)

- **OWASP ZAP**  
  - Пассивное/активное тестирование веб-приложений
  - API-сканирование
- **Nuclei**  
  - Шаблонно-ориентированное сканирование веб-сервисов 
  - Расширяемый набор шаблонов

## Требования

### Системные:

- Jenkins 2.492.2+  
- Docker 20.10+  
- Python 3.8+  
- Go 1.19+  
- Java 17+ (или 21+ с марта 2026) 
- Git

### Сетевые:

- Доступ к Docker Hub, GitHub, PyPI  
- API-доступ к DefectDojo  

### Права:

- Запуск Jenkins pipeline и Docker контейнеров  
- Доступ к Jenkins Credentials  
- API-ключ DefectDojo  

##  Установка

### 🔗 1. Подключение библиотеки в Jenkins

Jenkins > Manage Jenkins > Configure System > Global Pipeline Libraries:

```yaml
Name: security-library  
Default Version: main  
Retrieval method: Modern SCM  
SCM: Git  
Repository URL: https://your.git.repo  
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

1. Настройка переменных окружения в Jenkinsfile проекта:
   ```groovy
   environment {
       DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
       DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
   }
   ```

### Настройка сканеров

#### Semgrep
```
docker run --rm -v ${sourcePath}:/mnt/source:ro -v ${outputDir}:/mnt/output semgrep/semgrep semgrep --config=auto /mnt/source -o /mnt/output/${outputFile}
```

#### KICS
```
docker run --rm -v ${targetDir}:/path -v ${outputDir}:/reports checkmarx/kics:latest scan -p /path -o /reports/${outputFile}
```
#### Syft and Grype
```
syft ${imageName} -o json > ${outputDir}/${syftOutputFile}
grype sbom:${outputDir}/${syftOutputFile} -o json > ${outputDir}/${grypeOutputFile}
```

#### Nuclei
```
docker run --rm -v ${outputDir}:/output projectdiscovery/nuclei:latest \
        -u ${targetUrl} -o /output/${outputFile} -j
```
#### OWASP ZAP
```
docker run --rm -v ${outputDir}:/zap/wrk/ -t zaproxy/zap-stable \
        zap.sh -cmd -quickurl ${targetUrl} -quickout /zap/wrk/${outputFile}
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
       HOMEDIR = ''
       WORKSPACE = ''
       DEFECTDOJO_URL = 'https://your-defectdojo-instance.com'
       DEFECTDOJO_API_KEY = credentials('defectdojo-api-key')
   }
   ```

3. Используйте доступные функции сканирования в pipeline:

```groovy
stage('Semgrep Scan') {
    steps {
        runSemgrepScan(SOURCE_PATH, 'semgrep_report.json', HOME_DIR, WORKSPACE_PATH)
    }
}
```

### Использование Pipeline Script from SCM (рекомендуется)

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

## Структура проекта

- 📁 `scripts/uploader/`
  - `defectdojo_uploader.py` — скрипт на Python для отправки результатов сканирования в DefectDojo.
  - `Dockerfile` — окружение для запуска загрузчика.

- 📁 `src/org/security/scanners/`
  - (пусто) — директория зарезервирована для реализации классов сканеров.

- 📁 `vars/`
  - `runKICSScan.groovy` — сценарий запуска KICS.
  - `runNucleiScan.groovy` — сценарий запуска Nuclei.
  - `runSCAScan.groovy` — сценарий запуска анализа зависимостей (SCA).
  - `runSemgrepScan.groovy` — сценарий запуска Semgrep.
  - `runZAPScan.groovy` — сценарий запуска OWASP ZAP.
  - `uploadToDefectDojo.groovy` — сценарий для загрузки отчётов в DefectDojo. (НЕ ИСПОЛЬЗУЕТСЯ)
