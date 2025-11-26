FROM eclipse-temurin:21-jdk
LABEL authors="Eduard"

WORKDIR /app

COPY build/libs/mapforge-gameplay-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]