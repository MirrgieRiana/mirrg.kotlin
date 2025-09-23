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

    val task = tasks.register("generate${variantSuffix.uppercaseFirstChar().replace("-", "")}BuildScripts") {
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
    tasks.named("generateBuildScripts").configure { dependsOn(task) }

}
configureVariant("kotlin-1-7", "1.7.21", "17")
configureVariant("kotlin-1-8", "1.8.22", "17")
configureVariant("kotlin-1-9", "1.9.25", "21")
configureVariant("kotlin-2-0", "2.0.21", "21")
configureVariant("kotlin-2-1", "2.1.21", "21")
configureVariant("kotlin-2-2", "2.2.20", "21")
