import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by extra
buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.3.30"
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.3.RELEASE")
    }
}

plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "1.3.30"
//    id("org.jetbrains.kotlin.plugin.spring") version "1.2.51"
    id("org.springframework.boot") version "2.0.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}
apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
}

application {
    mainClassName = if (project.hasProperty("mainClass")) project.properties.get("mainClass").toString() else "examples.KotlinExampleKt"
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.0.2.RELEASE")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // The dependencyy below triggers an exception
//    implementation("ch.qos.logback:logback-classic:1.0.13")

//    implementation("io.projectreactor:reactor-core:3.1.8.RELEASE")

    implementation("io.projectreactor.kafka:reactor-kafka:1.0.0.RELEASE")

    implementation("com.natpryce:konfig:1.6.10.0")

//    compileOnly("org.springframework.boot:spring-boot-configuration-processor")
    compile("org.springframework.boot:spring-boot-starter")

    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
