# Используйте официальный образ OpenJDK
FROM openjdk:17-slim

# Установите рабочую директорию
WORKDIR /app

# Копируйте файл сборки (например, jar-файл) в контейнер
COPY build/libs/load_microservice-1.0-SNAPSHOT.jar app.jar

# Укажите команду для запуска приложения
CMD ["java", "-jar", "app.jar"]

# Откройте порт, на котором будет работать ваш сервер
EXPOSE 8099