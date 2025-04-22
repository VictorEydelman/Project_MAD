plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "2.3.5"
}

group = "ru.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Логирование
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.ktor:ktor-server-call-logging:2.3.5")
    // Ktor
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
application {
    mainClass = "ru.itmo.MainKt"
}
