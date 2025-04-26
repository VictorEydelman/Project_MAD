FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
RUN gradle dependencies --no-daemon

COPY . .
RUN gradle clean shadowJar --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/load_microservice.jar app.jar
COPY --from=builder /app/build/resources/main /app/resources/main
CMD ["java", "-jar", "app.jar"]
EXPOSE 8099