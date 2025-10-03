FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM openjdk:21-jdk-slim
ENV PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1
    
COPY --from=builder /app/build/libs/*T.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
