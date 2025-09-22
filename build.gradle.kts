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


fun configureSubBuild(subBuildName: String, versionString: String) {

    fun configureTask(taskName: String) {
        tasks.named(taskName) { dependsOn(gradle.includedBuild(subBuildName).task(":$taskName")) }
    }
    configureTask("clean")
    configureTask("build")
    configureTask("check")
    configureTask("publish")

    tasks.register("generate${subBuildName.uppercaseFirstChar().replace("-", "")}BuildScripts") {
        group = "generation"
        doLast {
            val files = project.layout.projectDirectory.dir("template").asFile.listFiles().filter { it.isFile && it.name.endsWith(".kts.txt") }
            files.forEach { inputFile ->
                val outputFile = gradle.includedBuild(subBuildName).projectDir.resolve(inputFile.name.removeSuffix(".txt"))
                println("Generating $outputFile")
                val input = inputFile.readText()
                val output = Template.evaluate(input, Template.Arguments(versionString))
                outputFile.writeText(output)
            }
        }
    }

}
configureSubBuild("kotlin-2-1", "2.1.0")
