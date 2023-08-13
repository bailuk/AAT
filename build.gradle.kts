
buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { url = uri("maven-local") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath("com.github.johnrengelman:shadow:8.1.1")
    }
}
