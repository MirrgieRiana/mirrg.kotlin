import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.5.20"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("org.apache.logging.log4j:log4j-core:2.17.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.code.gson:gson:2.9.0")
}
