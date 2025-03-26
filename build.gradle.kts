buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { url = uri("maven-local") }
    }

    dependencies {
        // https://developer.android.com/build/releases/gradle-plugin
        classpath("com.android.tools.build:gradle:8.8.2")

        // https://kotlinlang.org/docs/gradle-configure-project.html
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    }
}
