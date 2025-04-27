import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application

    // https://imperceptiblethoughts.com/shadow/getting-started
    id("com.gradleup.shadow") version "8.3.6"

    // https://kotlinlang.org/docs/gradle.html#targeting-the-jvm
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":aat-lib"))

    /**
     *    https://github.com/bailuk/foc
     */
    val focVersion : String by project
    implementation("com.github.bailuk.foc:foc:$focVersion")
    implementation("com.github.bailuk.foc:foc-extended:$focVersion")

    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    // same version as in mapsforge-poi-awt
    //implementation("org.xerial:sqlite-jdbc:3.28.0")

    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.2.224")

    /**
     *    https://github.com/mapsforge/mapsforge/blob/master/docs/Integration.md
     */
    val mapsForgeVersion: String by project
    val mapsForgeGtkVersion: String by project
    implementation("org.mapsforge:mapsforge-poi-awt:$mapsForgeVersion")
    implementation("com.github.bailuk:mapsforge-gtk:${mapsForgeGtkVersion}")

    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    /**
     *
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     *
     */
    val jupiterVersion: String by project
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
}

val appMainClass = "ch.bailu.aat_gtk.app.AppKt"

application {
    mainClass.set(appMainClass)
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("aat-gtk")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to appMainClass))
        }
    }
}


tasks.register<Exec>("generateGResource") {
    setWorkingDir("gresource")
    setCommandLine("./generate.sh")
}


tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        dependsOn(named("generateGResource"))
    }
}
