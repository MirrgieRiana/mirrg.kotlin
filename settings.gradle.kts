pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "mirrg.kotlin.helium"

fun includeIfGenerated(distributionSuffix: String) {
    if (rootProject.projectDir.resolve("$distributionSuffix/build.gradle.kts").exists()) includeBuild(distributionSuffix)
}
includeIfGenerated("kotlin-1-7")
includeIfGenerated("kotlin-1-8")
includeIfGenerated("kotlin-1-9")
includeIfGenerated("kotlin-2-0")
includeIfGenerated("kotlin-2-1")
includeIfGenerated("kotlin-2-2")
