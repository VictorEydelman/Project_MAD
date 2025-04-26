# Используйте официальный образ OpenJDK для сборки
FROM gradle:8.4-jdk17 AS build

# Установите рабочую директорию
WORKDIR /app

# Копируйте только файлы, необходимые для загрузки зависимостей
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle

# Загрузите зависимости
RUN gradle dependencies --no-daemon

# Теперь копируем остальные файлы приложения
COPY . .

# Выполните сборку приложения
RUN gradle clean build --no-daemon

# Используйте официальный образ OpenJDK для выполнения приложения
FROM openjdk:17-slim

# Установите рабочую директорию
WORKDIR /app

# Копируйте собранный jar-файл из предыдущего этапа
COPY --from=build /app/build/libs/logic_microservice-all.jar app.jar

# Укажите команду для запуска приложения
CMD ["java", "-jar", "app.jar"]

# Откройте порт, на котором будет работать ваш сервер
EXPOSE 8900
