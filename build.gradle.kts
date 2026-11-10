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


val generateWorkspacesTask = tasks.register("generateWorkspaces") {
    group = "generation"
}


val variantDir = project.layout.projectDirectory.dir("kotlin-1-9")

val generateVariantWorkspaceTask = tasks.register("generateKotlin19Workspace") {
    group = "generation"
}
generateWorkspacesTask.configure { dependsOn(generateVariantWorkspaceTask) }

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
            dependsOn(generateVariantWorkspaceTask)
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

val generateVariantWrapperTask = tasks.register<Wrapper>("generateKotlin19Wrapper") {
    group = "generation"
    scriptFile = variantDir.file("gradlew").asFile
    jarFile = variantDir.file("gradle/wrapper/gradle-wrapper.jar").asFile
    gradleVersion = "8.14.3"
    distributionType = Wrapper.DistributionType.BIN
}
generateVariantWorkspaceTask.configure { dependsOn(generateVariantWrapperTask) }

val generateVariantGradlePropertiesTask = tasks.register("generateKotlin19GradleProperties") {
    group = "generation"
    val outputFile = variantDir.file("gradle.properties").asFile
    outputs.file(outputFile)
    doLast {
        logger.lifecycle("Generating {}", outputFile)
        val properties = mapOf(
            "org.gradle.java.home" to javaToolchains.launcherFor {
                languageVersion = JavaLanguageVersion.of(21)
            }.get().metadata.installationPath.asFile.absolutePath,
            "org.gradle.jvmargs" to "-Xmx4g -XX:MaxMetaspaceSize=1g",
        )
        val string = properties
            .map { "${it.key}=${it.value.replace("\\", "\\\\")}" }
            .joinToString("") { "$it\n" }
        outputFile.parentFile.mkdirs()
        outputFile.writeText(string)
    }
}
generateVariantWorkspaceTask.configure { dependsOn(generateVariantGradlePropertiesTask) }
