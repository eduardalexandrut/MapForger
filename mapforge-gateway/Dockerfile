FROM eclipse-temurin:21-jdk
LABEL authors="Eduard"

WORKDIR /app

COPY build/libs/mapforge-gameplay-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]