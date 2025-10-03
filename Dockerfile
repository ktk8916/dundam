FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21-jdk-jammy

# Playwright 의존성 설치
RUN apt-get update && apt-get install -y \
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libxkbcommon0 \
    libxcomposite1 \
    libxdamage1 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libasound2 \
    libpango-1.0-0 \
    libcairo2 \
    libglib2.0-0 \
    libdbus-1-3 \
    libatspi2.0-0 \
    libgio-2.0-0 \
    libx11-6 \
    libxcb1 \
    libexpat1 \
    fonts-liberation
    
COPY --from=builder /app/build/libs/*T.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
