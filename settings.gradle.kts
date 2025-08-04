dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("maven-local") }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven { url = uri("maven-local") }
    }

    plugins {
        id("com.android.application") version "8.8.2"
        id("com.android.lint") version "8.8.2"

        id("org.jetbrains.kotlin.android") version "2.1.20"

        // https://imperceptiblethoughts.com/shadow/getting-started
        id("com.gradleup.shadow") version "8.3.6"

        // https://kotlinlang.org/docs/gradle-configure-project.html
        id("org.jetbrains.kotlin.jvm") version "2.1.20"
    }
}

include("ci")
include("aat-lib")
include("aat-gtk")
include("aat-android")
