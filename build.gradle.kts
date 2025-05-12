plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("jacoco")
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

jacoco {
	toolVersion = "0.8.12"
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport) // Генерировать отчёт после тестов
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // Сначала прогнать тесты
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework:spring-core")
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
}