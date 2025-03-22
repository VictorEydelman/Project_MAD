
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    implementation("io.ktor:ktor-server-tomcat-jakarta:2.3.4")
    implementation("io.ktor:ktor-server-html-builder:2.3.4") // Если вам нужно HTML-строительство
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    testImplementation(libs.kotlin.test.junit)
    testImplementation("io.ktor:ktor-server-test-host:2.0.0") // Замените на актуальную версию
    testImplementation("io.ktor:ktor-server-tests:2.0.0") // Замените на актуальную версию
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.10")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "mad.project.ApplicationKt" // Замените на полный путь к вашему классу, если он находится в пакете
    }
}
tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}



