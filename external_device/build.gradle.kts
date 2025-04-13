plugins {
    kotlin("jvm") version "2.1.20"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "0.1"

application {
    mainClass.set("org.example.MainKt")
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core:2.3.5")
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.shadowJar {
    archiveBaseName.set("external_device")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}