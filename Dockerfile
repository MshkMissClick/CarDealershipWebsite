# Этап сборки
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# 1. Копируем ВСЕ Gradle-файлы (с учетом обоих форматов)
COPY *.gradle* .
COPY gradle.properties* .
COPY gradlew* .
COPY gradle/ gradle/

# 2. Скачиваем зависимости
RUN [ -f build.gradle.kts ] && echo "Using Kotlin DSL" || echo "Using Groovy DSL"
RUN gradle dependencies --no-daemon || true

# 3. Копируем исходный код
COPY src/ src/

# 4. Собираем приложение
RUN gradle clean build -x test --no-daemon

# Финальный образ
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-jar", "app.jar"]