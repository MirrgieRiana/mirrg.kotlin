import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    java
}

val taskEntries = listOf(
    "build" to "build",
    "verification" to "check",
    "publishing" to "publish",
)

val subDistributions = listOf(
    "kotlin-2-1" to "kotlin21",
)

sourceSets {
    register("commonMain") {
        resources.srcDir("src/commonMain/template")
    }
    register("commonTest") {
        resources.srcDir("src/commonTest/template")
    }
}

tasks.register("publish") {
    group = "publishing"
}

fun subDistribution(dirName: String, name: String) {
    fun bridgeTask(group: String, taskName: String) {
        val checkTask by tasks.register<GradleBuild>("$taskName${name.uppercaseFirstChar()}") {
            this.group = group
            this.dir = file(dirName)
            this.tasks = listOf(":$taskName")
        }
        tasks.named(taskName).configure { dependsOn(checkTask) }
    }
    taskEntries.forEach {
        bridgeTask(it.first, it.second)
    }
}

subDistributions.forEach {
    subDistribution(it.first, it.second)
}
