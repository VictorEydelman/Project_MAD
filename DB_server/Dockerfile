FROM gradle:8.4-jdk17 as builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle
RUN gradle dependencies --no-daemon

COPY . .
RUN gradle clean shadowJar --no-daemon

FROM gradle:8.4-jdk17 as tester
WORKDIR /app
COPY --from=builder /app .
RUN gradle test --info

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/db_server.jar app.jar
COPY --from=builder /app/build/resources/main /app/resources
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080
# Используйте официальный образ OpenJDK
#FROM eclipse-temurin:17-jre-alpine

# Установите рабочую директорию
#WORKDIR /app

# Копируйте файл сборки (например, jar-файл) в контейнер
#COPY build/libs/db_server.jar app.jar

# Укажите команду для запуска приложения
#CMD ["java", "-jar", "app.jar"]

#EXPOSE 8080