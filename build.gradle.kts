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

tasks.register("publish") {
    group = "publishing"
}


fun configureSubBuild(distributionSuffix: String, kotlinVersion: String) {
    val includedBuild = if (gradle.includedBuilds.any { it.name == distributionSuffix }) gradle.includedBuild(distributionSuffix) else null

    if (includedBuild != null) {
        fun configureTask(taskName: String) {
            tasks.named(taskName) { dependsOn(includedBuild.task(":$taskName")) }
        }
        configureTask("clean")
        configureTask("build")
        configureTask("check")
        configureTask("publish")
    }

    tasks.register("generate${distributionSuffix.uppercaseFirstChar().replace("-", "")}BuildScripts") {
        group = "generation"
        doLast {
            val inputDir = project.layout.projectDirectory.dir("template")
            val outputDir = project.layout.projectDirectory.dir(distributionSuffix)

            val files = inputDir.asFile.listFiles().filter { it.isFile && it.name.endsWith(".kts.txt") }
            files.forEach { inputFile ->
                val outputFile = outputDir.file(inputFile.name.removeSuffix(".txt")).asFile
                println("Generating $outputFile")
                val input = inputFile.readText()
                val arguments = Template.Arguments(
                    versionString = kotlinVersion,
                    parameters = mapOf(
                        "distributionSuffix" to distributionSuffix,
                        "kotlinVersion" to kotlinVersion,
                    ),
                )
                val output = Template.evaluate(input, arguments)
                outputFile.writeText(output)
            }
        }
    }

}
configureSubBuild("kotlin-2-1", "2.1.21")
