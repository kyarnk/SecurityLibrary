# # Dockerfile
# FROM python:3.9-slim

# # Установка зависимостей
# # Обновляем pip и устанавливаем requests
# RUN python -m pip install --upgrade pip && \
#     pip install --no-cache-dir requests

# # Создание рабочей директории и копирование скрипта
# WORKDIR /app
# COPY upload_reports.py semgrep.json /app 


# # # Опционально: Устанавливаем права на выполнение скрипта
# # RUN chmod +x /app/upload_reports.py


# EXPOSE 8080
# # Установка точки входа (необязательно, но удобно)
# ENTRYPOINT ["python3"]
# CMD ["/app/upload_reports.py", "/app/semgrep.json"]

FROM python:3.11-slim

WORKDIR /app
COPY upload_reports.py .

RUN pip install requests

ENTRYPOINT ["python", "upload_reports.py"]
