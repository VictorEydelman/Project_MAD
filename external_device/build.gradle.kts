plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.2"
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
    implementation("io.ktor:ktor-server-core:3.1.2")
    implementation("io.ktor:ktor-server-netty:3.1.2")
    implementation("io.ktor:ktor-server-content-negotiation:3.1.2")
    implementation("io.ktor:ktor-serialization-jackson:3.1.2")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}


tasks.test {
    useJUnitPlatform()
}