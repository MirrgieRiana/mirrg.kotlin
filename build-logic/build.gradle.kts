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
    jvmToolchain { // (int)バージョンは1.6バリアントでは利用できない
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
