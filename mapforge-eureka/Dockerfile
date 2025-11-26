FROM eclipse-temurin:21-jdk
LABEL authors="Eduard"

WORKDIR /app

COPY build/libs/mapforge-eureka-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]