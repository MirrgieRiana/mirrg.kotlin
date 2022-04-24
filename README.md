[![Build](https://github.com/MirrgieRiana/mirrg.kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/MirrgieRiana/mirrg.kotlin/actions/workflows/build.yml)

# Include

To include the source files, download each source file individually.

```
tasks {
    register("fetchMirrgKotlin") {
        fun fetch(fileName: String) {
            val file = File("src/main/java").resolve(fileName)
            when {
                file.parentFile.isDirectory -> Unit
                file.parentFile.exists() -> throw RuntimeException("Already exists: ${file.parentFile}")
                !file.parentFile.mkdirs() -> throw RuntimeException("Could not create the directory: ${file.parentFile}")
            }
            file.writeBytes(URL("https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/main/src/main/java/$fileName").readBytes())
        }
        fetch("mirrg/kotlin/log4j/hydrogen/Logging.kt")
    }
}
```
