plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.maven.resolver:maven-resolver-util:1.9.19")
}

kotlin {
    jvmToolchain(17)
}
