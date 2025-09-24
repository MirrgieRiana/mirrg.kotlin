# mirrg.kotlin.helium

A utility library available across Kotlin Multiplatform targets.

## Installation

This library is published to a Maven repository hosted on GitHub.

Repository:

```
https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/refs/heads/maven/maven/
```

Maven coordinates:

```
mirrg.kotlin:mirrg.kotlin.helium-kotlin-1-6:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-1-7:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-1-8:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-1-9:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-2-0:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-2-1:<version>
mirrg.kotlin:mirrg.kotlin.helium-kotlin-2-2:<version>
```

Choose the artifact ID according to the Kotlin version you are using.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/refs/heads/maven/maven/")
}

dependencies {
    implementation("mirrg.kotlin:mirrg.kotlin.helium-kotlin-2-2:<version>")
}
```

## Legacy Versions

The following `mirrg.kotlin.helium` artifact is deprecated.

```kotlin
repositories {
    maven("https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/refs/heads/maven/maven/")
}

dependencies {
    implementation("mirrg.kotlin:mirrg.kotlin.helium:<version>")
}
```

### Kotlin Version Compatibility

The Kotlin compiler version each library release is built with.

| Artifact ID         | Version | Kotlin Version |
|---------------------|---------|----------------|
| mirrg.kotlin.helium | 1.0.0   | 1.9.25         |
| mirrg.kotlin.helium | 1.1.0   | 1.9.25         |
| mirrg.kotlin.helium | 2.0.0   | 2.0.21         |
| mirrg.kotlin.helium | 3.0.0   | 2.1.21         |
