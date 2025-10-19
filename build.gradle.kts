plugins {
    id("com.android.application") version "8.8.2" apply(false)
    id("com.android.lint") version "8.8.2" apply(false)

    id("org.jetbrains.kotlin.android") version "2.1.20" apply(false)

    // https://imperceptiblethoughts.com/shadow/getting-started
    id("com.gradleup.shadow") version "8.3.6" apply(false)

    // https://kotlinlang.org/docs/gradle-configure-project.html
    id("org.jetbrains.kotlin.jvm") version "2.1.20" apply(false)
}
