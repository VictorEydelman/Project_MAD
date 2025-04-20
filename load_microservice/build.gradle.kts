plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("io.ktor:ktor-server-core-jvm:2.3.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.4")
    implementation("io.ktor:ktor-client-core-jvm:2.3.4")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("redis.clients:jedis:5.2.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("io.ktor:ktor-client-jackson:2.3.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-jackson:2.3.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")

    // ИЛИ для kotlinx.serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    implementation("ch.qos.logback:logback-classic:1.4.8")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.4")
    testImplementation("io.ktor:ktor-client-mock-jvm:2.3.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

tasks.shadowJar {
    archiveBaseName.set("load_microservice")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}