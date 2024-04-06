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


task<JavaExec>("property2config") {
    dependsOn(tasks.named("build"))
    description = "Generate configuration class from gradle property"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("property2config.MainKt")
    workingDir(project.rootDir)
}

task<JavaExec>("generateStrings") {
    description = "Generate Kotlin string resource classes from Android string resources"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("generate_strings.MainKt")
    workingDir(project.rootDir)
}


task<JavaExec>("generateImageMapping") {
    dependsOn(":aat-android:processReleaseResources")
    description = "Generate image mapping from R.txt"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("generate_image_mapping.MainKt")
    workingDir(project.rootDir)

}
