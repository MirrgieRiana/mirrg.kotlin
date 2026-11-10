# mirrg.kotlin.lithium

A utility library available across Kotlin Multiplatform targets.

## Installation

This library is published to a Maven repository hosted on GitHub.

Repository:

```
https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/refs/heads/maven/maven/
```

Maven coordinates:

```
mirrg.kotlin:mirrg.kotlin.lithium:<version>
```

Choose the artifact ID according to the Kotlin version you are using.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/refs/heads/maven/maven/")
}

dependencies {
    implementation("mirrg.kotlin:mirrg.kotlin.lithium:<version>")
}
```

### Kotlin Version Compatibility

The Kotlin compiler version each library release is built with.

| Artifact ID          | Version | Kotlin Version |
|----------------------|---------|----------------|
| mirrg.kotlin.lithium | 4.6.0   | 1.9.25         |
