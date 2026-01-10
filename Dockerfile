# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

RUN ./gradlew --version

COPY src ./src

RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ARG SERVER_PORT=8080

ENV PORT=${SERVER_PORT}

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java","-jar","app.jar"]
