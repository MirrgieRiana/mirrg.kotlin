import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    java
}

sourceSets {
    register("commonMain") {
        resources.srcDir("template/src/commonMain/template")
    }
    register("commonTest") {
        resources.srcDir("template/src/commonTest/template")
    }
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

fun configureVariant(
    variantSuffix: String,
    gradleVersion: String,
    gradleJavaVersion: Int,
) {
    val variantSlug = variantSuffix.replace("-", "").uppercaseFirstChar()
    val variantDir = project.layout.projectDirectory.dir(variantSuffix)

    val generateVariantWorkspaceTask = tasks.register("generate${variantSlug}Workspace") {
        group = "generation"
    }
    generateWorkspacesTask.configure { dependsOn(generateVariantWorkspaceTask) }

    // Variantプロジェクトのタスクのブリッジ
    run {
        fun configureTask(group: String, taskName: String) {
            val execTask = tasks.register<Exec>("exec${taskName.uppercaseFirstChar()}${variantSlug}") {
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

    val generateVariantBuildScriptsTask = tasks.register("generate${variantSlug}BuildScripts") {
        group = "generation"
        val templateDir = project.layout.projectDirectory.dir("template")
        val inputFiles = project.fileTree(templateDir) { include("*.kts") }
        inputs.files(inputFiles)
        outputs.dir(variantDir)
        doLast {
            inputFiles.files.forEach { inputFile ->
                val outputFile = variantDir.file(inputFile.name).asFile
                logger.lifecycle("Generating {}", outputFile)
                val input = inputFile.readText()
                outputFile.parentFile.mkdirs()
                outputFile.writeText(input)
            }
        }
    }
    generateVariantWorkspaceTask.configure { dependsOn(generateVariantBuildScriptsTask) }

    val generateVariantWrapperTask = tasks.register<Wrapper>("generate${variantSlug}Wrapper") {
        group = "generation"
        scriptFile = variantDir.file("gradlew").asFile
        jarFile = variantDir.file("gradle/wrapper/gradle-wrapper.jar").asFile
        this.gradleVersion = gradleVersion
        distributionType = Wrapper.DistributionType.BIN
    }
    generateVariantWorkspaceTask.configure { dependsOn(generateVariantWrapperTask) }

    val generateVariantGradlePropertiesTask = tasks.register("generate${variantSlug}GradleProperties") {
        group = "generation"
        val outputFile = variantDir.file("gradle.properties").asFile
        outputs.file(outputFile)
        doLast {
            logger.lifecycle("Generating {}", outputFile)
            val properties = mapOf(
                "org.gradle.java.home" to javaToolchains.launcherFor {
                    languageVersion = JavaLanguageVersion.of(gradleJavaVersion)
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

}
configureVariant("kotlin-1-9", "8.14.3", 21)
