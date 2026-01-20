import java.util.Properties

plugins {
    java
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    //Eureka
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    //JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //Postgres
    runtimeOnly("org.postgresql:postgresql")

    //Hibernate tools
    implementation("org.hibernate:hibernate-core:6.3.0.Final")

    //Liquibase
    //    implementation("org.liquibase:liquibase-core")
    // Dependencies required for running liquibase for tests
    liquibaseRuntime("org.liquibase:liquibase-core:4.23.0")
    liquibaseRuntime("info.picocli:picocli:4.7.4")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:4.22.0")
    liquibaseRuntime("org.postgresql:postgresql:42.7.3")
    liquibaseRuntime(files(sourceSets["main"].output))
    liquibaseRuntime(files(sourceSets["main"].compileClasspath))
    liquibaseRuntime(files(sourceSets["main"].runtimeClasspath))

    //Dotenv
    implementation("io.github.cdimascio:dotenv-java:3.0.0")

}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    //Load values from .env
    val envFile = file(".env")
    if (envFile.exists()) {
        envFile.forEachLine { line ->
            val parts = line.split("=", limit = 2)
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                if (System.getenv(key) == null) {
                    System.setProperty(key, value)
                }
            }
        }
    }

    fun env(key: String): String =
        System.getenv(key) ?: System.getProperty(key) ?: error("Missing env var: $key")

    liquibase {
        activities.register("main") {
            arguments = mapOf(
                // 1. The relative path from the searchPath
                "changeLogFile" to "db/changelog/db.changelog-master.xml",

                // 2. Add the searchPath to include your resources folder
                "searchPath" to "${project.projectDir}/src/main/resources",
                "url" to env("DB_URL"),
                "username" to env("DB_USER"),
                "password" to env("DB_PASSWORD"),
                "driver" to "org.postgresql.Driver"
            )
        }
        runList = "main"

    }
}

val props = Properties().apply {
    load(file("src/main/resources/application.yaml").inputStream())
}
