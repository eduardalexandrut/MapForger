import java.util.Properties

plugins {
    java
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.liquibase.gradle") version "2.2.0"
}

// --- LOCAL ENVIRONMENT LOADING ---
val localEnv = mutableMapOf<String, String>()
val envFile = file("${project.projectDir}/.env")

if (envFile.exists()) {
    envFile.forEachLine { line ->
        val parts = line.split("=", limit = 2)
        if (parts.size == 2) {
            localEnv[parts[0].trim()] = parts[1].trim()
        }
    }
}

fun env(key: String): String =
    System.getenv(key) ?: localEnv[key] ?: error("Missing env var: $key in ${project.name}")
// ---------------------------------

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
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Eureka & OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Postgres & Hibernate
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-core:6.3.0.Final")

    // Liquibase
    implementation("org.liquibase:liquibase-core")
    liquibaseRuntime("org.liquibase:liquibase-core:4.23.0")
    liquibaseRuntime("info.picocli:picocli:4.7.4")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:4.22.0")
    liquibaseRuntime("org.postgresql:postgresql:42.7.3")
    liquibaseRuntime(files(sourceSets["main"].output))
    liquibaseRuntime(files(sourceSets["main"].compileClasspath))
    liquibaseRuntime(files(sourceSets["main"].runtimeClasspath))

    // Testcontainers
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")

    // Dotenv
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
}

println("GAMEPLAY LIQUIBASE URL = ${env("GAMEPLAY_DB_URL")}")

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to "db/changelog/db.changelog-master.xml",
            "searchPath" to "${project.projectDir}/src/main/resources",
            "url" to env("GAMEPLAY_DB_URL"),
            "username" to env("POSTGRES_USER"),
            "password" to env("POSTGRES_PASSWORD"),
            "driver" to "org.postgresql.Driver"
        )
    }
    runList = "main"
}