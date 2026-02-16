dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

include("ci")
include("aat-lib")
include("aat-gtk")
include("aat-android")
