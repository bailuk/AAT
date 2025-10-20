import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    id("org.jetbrains.kotlin.jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.register<JavaExec>("property2config") {
    dependsOn(tasks.named("build"))
    description = "Generate configuration class from gradle property"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("property2config.MainKt")
    workingDir(project.rootDir)
}

tasks.register<JavaExec>("generateStrings") {
    description = "Generate Kotlin string resource classes from Android string resources"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("generate_strings.MainKt")
    workingDir(project.rootDir)
}

tasks.register<JavaExec>("generateImageMapping") {
    dependsOn(":aat-android:processReleaseResources")
    description = "Generate image mapping from R.txt"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("generate_image_mapping.MainKt")
    workingDir(project.rootDir)

}
