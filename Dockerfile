FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM mcr.microsoft.com/playwright/java:v1.55.0
    
COPY --from=builder /app/build/libs/*T.jar app.jar
RUN java -cp app.jar com.microsoft.playwright.CLI install-deps chromium

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
