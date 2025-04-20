FROM gradle:8-jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle clean shadowJar

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/load_microservice.jar app.jar
COPY --from=builder /app/build/resources/main /app/resources/main
CMD ["java", "-jar", "app.jar"]
EXPOSE 8099