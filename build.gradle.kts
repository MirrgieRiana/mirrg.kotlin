import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    kotlin("multiplatform") version "1.9.25"
    `maven-publish`
}

group = "mirrg.kotlin"
val envVersion = System.getenv("VERSION") ?: ""
if (envVersion.isNotBlank()) version = envVersion

kotlin {
    val jvmVersion = 21
    jvmToolchain(jvmVersion)

    jvm {
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, jvmVersion)
    }
    js {
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
        @Suppress("unused")
        val commonMain by getting {
            kotlin.setSrcDirs(listOf("generated/commonMain/kotlin"))
        }

        @Suppress("unused")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
            kotlin.setSrcDirs(listOf("generated/commonTest/kotlin"))
        }
    }
}

// 指定しなければ暗黙の org.gradle.api.tasks.bundling.Jar の方が読み込まれて指定範囲が狭まる
tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    from(layout.projectDirectory.file("LICENSE")) {
        into("META-INF")
        rename { "LICENSE" }
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
