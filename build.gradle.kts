
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mongo_driver_version: String by project
val koin_version: String by project
val jbcrypt_version: String by project
val kotlinx_datetime_version: String by project
val kotlinx_serialization_version: String by project

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("io.ktor.plugin") version "2.3.11"
}

group = "com.julia.imp"
version = "0.0.1"

application {
    mainClass.set("com.julia.imp.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-http-redirect-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:$mongo_driver_version")
    implementation("org.mongodb:bson-kotlinx:$mongo_driver_version")

    // Koin Dependency Injection
    implementation("io.insert-koin:koin-ktor:$koin_version")

    // BCrypt implementation
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")

    // KotlinX date time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
