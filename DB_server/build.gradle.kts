
plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "mad.project"
version = "0.0.1"

application {
    mainClass.set("mad.project.ApplicationKt") // Замените на полный путь к вашему классу, если он находится в пакете

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.ktor.server.tomcat.jakarta)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
    implementation("ru.yandex.clickhouse:clickhouse-jdbc:0.1.36")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("io.ktor:ktor-server-tomcat-jakarta:2.3.4")
    implementation("io.ktor:ktor-server-html-builder:2.3.4") // Если вам нужно HTML-строительство
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0") // Замените на последнюю версию
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    testImplementation(libs.kotlin.test.junit)
    implementation("redis.clients:jedis:5.2.0")
    testImplementation("io.ktor:ktor-server-test-host:2.0.0") // Замените на актуальную версию
    testImplementation("io.ktor:ktor-server-tests:2.0.0") // Замените на актуальную версию
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.10")

}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("mad.project.ApplicationKt")
}
tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}
tasks.shadowJar {
    archiveBaseName.set("db_server")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "mad.project.ApplicationKt"
    }
}

