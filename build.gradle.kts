plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"

    id("io.ktor.plugin") version "2.3.5"
    id("com.github.gmazzo.buildconfig") version "4.1.2"

    application
}

group = "org.urielserv.uriel"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.klogging:klogging-jvm:0.5.6")
    implementation("io.klogging:slf4j-klogging:0.5.6")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-websockets")

    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("com.akuleshov7:ktoml-core:0.5.0")
    implementation("com.akuleshov7:ktoml-file:0.5.0")
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("org.urielserv.uriel.MainKt")
}

buildConfig {
    buildConfigField("String", "VERSION", "\"${project.version}\"")
}