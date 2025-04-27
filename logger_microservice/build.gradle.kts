plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "8.3.6"
}

group = "ru.itmo"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("redis.clients:jedis:5.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    testImplementation(kotlin("test"))
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "ru.itmo.MainKt"
    }
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}