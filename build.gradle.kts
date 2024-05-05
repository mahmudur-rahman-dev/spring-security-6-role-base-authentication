import java.text.SimpleDateFormat
import java.util.*

plugins {
    java
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "global.cinet"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.security:spring-security-test")
//    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation ("org.projectlombok:lombok")
    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")

    implementation("mysql:mysql-connector-java:8.0.27")


    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.4.2.Final")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:6.4.0.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    enabled = false
    useJUnitPlatform()
}

tasks.register<Delete>("cleanJar") {
    delete("target/")
}

tasks.bootJar {
    dependsOn("cleanJar")

    val timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())
    archiveFileName.set("signage.jar")
    destinationDirectory.set(file("target/"))
}
