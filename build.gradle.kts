plugins {
    kotlin("multiplatform") version "1.9.25"
    `maven-publish`
}

group = "mirrg.kotlin"
val envVersion = System.getenv("VERSION") ?: ""
if (envVersion.isNotBlank()) version = envVersion

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
    linuxX64()
    linuxArm64()
    mingwX64()
    //macosX64()
    //macosArm64()
    //androidNativeArm32()
    //androidNativeArm64()
    //androidNativeX86()
    //androidNativeX64()
    //iosX64()
    //iosArm64()
    //iosSimulatorArm64()
    //tvosX64()
    //tvosArm64()
    //tvosSimulatorArm64()
    //watchosX64()
    //watchosArm64()
    //watchosSimulatorArm64()
    //watchosArm32()
    //@OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    //wasmJs {
    //    browser()
    //    nodejs()
    //    d8()
    //}
    //@OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    //wasmWasi()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "localFolder"
            val envMavenDir = System.getenv("MAVEN_DIR") ?: ""
            url = if (envMavenDir.isNotBlank()) uri(file(envMavenDir)) else uri(layout.projectDirectory.dir("maven"))
        }
    }
}
