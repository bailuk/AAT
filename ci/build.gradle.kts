plugins {
    application

    // https://kotlinlang.org/docs/gradle.html#targeting-the-jvm
    kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}


tasks.register("property2config", JavaExec::class) {
    dependsOn("build")
    description = "Generate configuration class from gradle property"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("property2config.MainKt")

    args(
        setOf (
            "${project.rootDir}/gradle.properties",
            "${project.rootDir}/aat-lib/src/main/java/ch/bailu/aat_lib/Configuration.java"
        )
    )
}
