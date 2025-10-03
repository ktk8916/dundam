FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM openjdk:21-jdk-slim

COPY --from=builder /app/build/libs/*T.jar app.jar
RUN apt-get update && apt-get install -y wget curl \
    && java -cp app.jar com.microsoft.playwright.CLI install chromium \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
