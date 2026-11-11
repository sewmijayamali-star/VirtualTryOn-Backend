
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.ZeRo"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)



    // PostgreSQL database
    implementation("org.jetbrains.exposed:exposed-core:1.5.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.5.0")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("com.zaxxer:HikariCP:7.0.2")

    // JSON
    implementation("io.ktor:ktor-server-content-negotiation:3.6.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.6.0")

    implementation("org.mindrot:jbcrypt:0.4")

    // HTTP client for FASHN API requests
    implementation("io.ktor:ktor-client-core:3.6.0")
    implementation("io.ktor:ktor-client-cio:3.6.0")

    // token jwt
    implementation("io.ktor:ktor-server-auth:3.6.0")
    implementation("io.ktor:ktor-server-auth-jwt:3.6.0")
    implementation("com.auth0:java-jwt:4.5.0")

    implementation("io.ktor:ktor-client-content-negotiation:3.6.0")

    // Ktor HTTP Client

    implementation("io.ktor:ktor-serialization-kotlinx-json:3.6.0")


    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}