import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"

    id("com.netflix.dgs.codegen") version "6.1.1"

    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:8.2.0")
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Misc
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    generateClient = true
    generateKotlinClosureProjections = true
    generateKotlinNullableClasses = true
    packageName = "com.example.demo.generated"
    language = "kotlin"
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version = "1.0.0"
    filter {
        include("**/src/**/*.kt", "**.kts")
        exclude("**/build/**/*.kt", "**/generated/**/*.kt")
    }
}
