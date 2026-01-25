FROM eclipse-temurin:21-jdk
LABEL authors="Eduard"

WORKDIR /app

COPY build/libs/mapforge-core-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]