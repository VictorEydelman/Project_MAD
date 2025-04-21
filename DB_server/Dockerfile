FROM gradle:8-jdk17 as builder
WORKDIR /app
COPY . .

RUN gradle clean shadowJar --no-daemon

FROM gradle:8-jdk17 as tester
WORKDIR /app
COPY --from=builder /app .
RUN gradle test --info

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/db_server.jar app.jar
COPY --from=builder /app/build/resources/main /app/resources
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080
# Используйте официальный образ OpenJDK
#FROM openjdk:17-slim

# Установите рабочую директорию
#WORKDIR /app

# Копируйте файл сборки (например, jar-файл) в контейнер
#COPY build/libs/db_server.jar app.jar

# Укажите команду для запуска приложения
#CMD ["java", "-jar", "app.jar"]

