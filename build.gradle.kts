import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    java
}

tasks.register("generate") {
    group = "generation"
}

tasks.register("publish") {
    group = "publishing"
}


val variantDir = project.layout.projectDirectory.dir("kotlin-1-9")

// Variantプロジェクトのタスクのブリッジ
run {
    fun configureTask(group: String, taskName: String) {
        val execTask = tasks.register<Exec>("exec${taskName.uppercaseFirstChar()}Kotlin19") {
            this.group = group
            workingDir = variantDir.asFile
            val isWindows = System.getProperty("os.name").contains("windows", ignoreCase = true)
            if (isWindows) {
                commandLine("cmd", "/c", variantDir.file("gradlew.bat").asFile.absolutePath, taskName)
            } else {
                doFirst {
                    variantDir.file("gradlew").asFile.setExecutable(true)
                }
                commandLine(variantDir.file("gradlew").asFile.absolutePath, taskName)
            }
        }
        tasks.named(taskName).configure { dependsOn(execTask) }
    }
    configureTask("generation", "generate")
    configureTask("build", "clean")
    configureTask("build", "assemble")
    configureTask("verification", "check")
    configureTask("build", "build")
    configureTask("publishing", "publish")
}
