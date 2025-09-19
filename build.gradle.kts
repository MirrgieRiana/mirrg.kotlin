import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform") version "2.1.21" apply false
}

project(":kotlin-2-1") {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "maven-publish")

    group = "mirrg.kotlin"
    val envVersion = System.getenv("VERSION") ?: ""
    if (envVersion.isNotBlank()) version = envVersion

    repositories {
        mavenCentral()
    }

    extensions.configure<KotlinMultiplatformExtension> {
        jvm()
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
            val commonMain by getting
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }
        sourceSets.configureEach {
            kotlin.setSrcDirs(listOf(project.layout.projectDirectory.dir("generated/$name/kotlin").asFile))
            resources.setSrcDirs(listOf(project.layout.projectDirectory.dir("generated/$name/resources").asFile))
        }
    }

    tasks.withType<Jar>().configureEach {
        from(rootProject.file("LICENSE")) {
            into("META-INF")
            rename { "LICENSE" }
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "localFolder"
                val envMavenDir = System.getenv("MAVEN_DIR") ?: ""
                url = if (envMavenDir.isNotBlank()) uri(file(envMavenDir)) else uri(rootProject.layout.projectDirectory.dir("maven"))
            }
        }
    }

}
