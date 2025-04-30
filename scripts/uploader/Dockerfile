# Dockerfile
FROM python:3.9-slim

# Установка зависимостей
# Обновляем pip и устанавливаем requests
RUN python -m pip install --upgrade pip && \
    pip install --no-cache-dir requests

# Создание рабочей директории и копирование скрипта
WORKDIR /app
COPY defectdojo_uploader.py /app/

# Опционально: Устанавливаем права на выполнение скрипта
RUN chmod +x /app/defectdojo_uploader.py

# Установка точки входа (необязательно, но удобно)
ENTRYPOINT ["python", "/app/defectdojo_uploader.py"] 