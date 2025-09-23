import org.gradle.kotlin.dsl.support.uppercaseFirstChar

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

tasks.register("generate") {
    group = "generation"
}

tasks.register("publish") {
    group = "publishing"
}


tasks.register("generateBuildScripts") {
    group = "generation"
}

fun configureVariant(
    variantSuffix: String,
    kotlinVersion: String,
    jvmTarget: String,
    gradleVersion: String,
) {
    val includedBuild = if (gradle.includedBuilds.any { it.name == variantSuffix }) gradle.includedBuild(variantSuffix) else null

    if (includedBuild != null) {
        fun configureTask(taskName: String) {
            tasks.named(taskName).configure { dependsOn(includedBuild.task(":$taskName")) }
        }
        configureTask("generate")
        configureTask("clean")
        configureTask("assemble")
        configureTask("check")
        configureTask("build")
        configureTask("publish")
    }

    val generateBuildScriptsTask = tasks.register("generate${variantSuffix.uppercaseFirstChar().replace("-", "")}BuildScripts") {
        group = "generation"
        doLast {
            val inputDir = project.layout.projectDirectory.dir("template")
            val outputDir = project.layout.projectDirectory.dir(variantSuffix)

            val files = inputDir.asFile.listFiles().filter { it.isFile && it.name.endsWith(".kts.txt") }
            files.forEach { inputFile ->
                val outputFile = outputDir.file(inputFile.name.removeSuffix(".txt")).asFile
                println("Generating $outputFile")
                val input = inputFile.readText()
                val arguments = Template.Arguments(
                    versionString = kotlinVersion,
                    parameters = mapOf(
                        "variantSuffix" to variantSuffix,
                        "kotlinVersion" to kotlinVersion,
                        "jvmTarget" to jvmTarget,
                    ),
                )
                val output = Template.evaluate(input, arguments)
                outputFile.parentFile.mkdirs()
                outputFile.writeText(output)
            }
        }
    }
    tasks.named("generateBuildScripts").configure { dependsOn(generateBuildScriptsTask) }

    val generateWrapperTask = tasks.register<Wrapper>("generate${variantSuffix.uppercaseFirstChar().replace("-", "")}Wrapper") {
        val outputDir = project.layout.projectDirectory.dir(variantSuffix)
        scriptFile = outputDir.file("gradlew").asFile
        jarFile = outputDir.file("gradle/wrapper/gradle-wrapper.jar").asFile
        this.gradleVersion = gradleVersion
        distributionType = Wrapper.DistributionType.BIN
    }
    tasks.named("generateBuildScripts").configure { dependsOn(generateWrapperTask) }

}
configureVariant("kotlin-1-7", "1.7.21", "17", "8.14.3")
configureVariant("kotlin-1-8", "1.8.22", "17", "8.14.3")
configureVariant("kotlin-1-9", "1.9.25", "21", "8.14.3")
configureVariant("kotlin-2-0", "2.0.21", "21", "8.14.3")
configureVariant("kotlin-2-1", "2.1.21", "21", "8.14.3")
configureVariant("kotlin-2-2", "2.2.20", "21", "8.14.3")
