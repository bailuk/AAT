
buildscript {
    repositories {
        mavenCentral()
        google()
        maven { url = uri("maven-local") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
    }
}
