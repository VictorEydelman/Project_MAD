# Используйте официальный образ OpenJDK
#FROM openjdk:17-slim
#
## Установите рабочую директорию
#WORKDIR /app
FROM gradle:8-jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle clean shadowJar

FROM openjdk:17-slim
WORKDIR /app
# Копируйте файл сборки (например, jar-файл) в контейнер
COPY build/libs/external_device.jar app.jar

# Укажите команду для запуска приложения
CMD ["java", "-jar", "app.jar"]

# Откройте порт, на котором будет работать ваш сервер
EXPOSE 8909