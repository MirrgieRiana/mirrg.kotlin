import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

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

        fun KotlinSourceSet.setGeneration() {
            val sourceSetName = name
            val generateTask = project.tasks.create<Sync>("generate${sourceSetName.uppercaseFirstChar()}KotlinSources") {
                group = "build"
                into(project.layout.projectDirectory.dir("generated/$sourceSetName/kotlin"))
                from(rootProject.layout.projectDirectory.dir("src/$sourceSetName/template")) {
                    include("**/*.txt")
                    rename { it.removeSuffix(".txt") }
                }
            }
            kotlin.setSrcDirs(listOf(generateTask))
        }

        sourceSets {
            val commonMain by getting {
                setGeneration()
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
                setGeneration()
            }
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
