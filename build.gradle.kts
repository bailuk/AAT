// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath( "com.android.tools.build:gradle:4.1.3")
    }
}


allprojects {
    repositories {
        maven {
            setUrl("https://jitpack.io")
        }
    }
}
