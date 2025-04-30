FROM gradle:8.4-jdk17 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

RUN gradle dependencies --no-daemon

COPY . .

RUN gradle clean shadowJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/logger_microservice-1.0-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
