plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"


dependencies {

	// WebFlux engine (Gateway requires WebFlux)
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	implementation("org.springframework.cloud:spring-cloud-starter-gateway")

	// Eureka client for service discovery
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

	// Basic starter (logging, auto-config)
	implementation("org.springframework.boot:spring-boot-starter")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
