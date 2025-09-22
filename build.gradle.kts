plugins {
    java
    id("build-logic")
}

sourceSets {
    register("commonMain") {
        resources.srcDir("template/src/commonMain/template")
    }
    register("commonTest") {
        resources.srcDir("template/src/commonTest/template")
    }
}

tasks.register("publish") {
    group = "publishing"
}

fun configureSubBuild(subBuildName: String) {
    fun configureTask(taskName: String) {
        tasks.named(taskName) { dependsOn(gradle.includedBuild(subBuildName).task(":$taskName")) }
    }
    configureTask("clean")
    configureTask("build")
    configureTask("check")
    configureTask("publish")
}
configureSubBuild("kotlin-2-1")
