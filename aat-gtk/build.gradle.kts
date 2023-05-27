plugins {
    application

    // https://imperceptiblethoughts.com/shadow/getting-started
    id("com.github.johnrengelman.shadow") version "8.1.1"

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

tasks.test {
    useJUnitPlatform()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.1") {
            exclude("com.android.tools.build")
        }
    }
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    outputs.upToDateWhen { false }
    libraryjars("${System.getProperty("java.home")}/jmods/java.base.jmod")
    configuration("proguard.pro")
}

tasks.build.get().finalizedBy(tasks.getByName("proguard"))

dependencies {
    implementation(project(":aat-lib"))

    /**
     *    https://github.com/bailuk/foc
     */
    val focVersion : String by project
    implementation("com.github.bailuk.foc:foc:$focVersion")
    //implementation("ch.bailu:foc:$focVersion")

    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    // same version as in mapsforge-poi-awt
    //implementation("org.xerial:sqlite-jdbc:3.28.0")

    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.0.204")

    /**
     *    https://github.com/taimos/GPSd4Java
     *    https://wiki.mobian-project.org/doku.php?id=location
     */
    implementation("de.taimos:gpsd4java:1.10")

    /**
     *    https://github.com/mapsforge/mapsforge/blob/master/docs/Integration.md
     */
    val mapsForgeVersion: String by project
    val mapsForgeGtkVersion: String by project
    implementation("org.mapsforge:mapsforge-poi-awt:$mapsForgeVersion")
    implementation("com.github.bailuk:mapsforge-gtk:${mapsForgeGtkVersion}")
    // implementation("org.mapsforge:mapsforge-map-gtk:SNAPSHOT")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    /**
     *
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     *
     */
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
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


task<Exec>("generateGResource") {
    setCommandLine("gresource/generate.sh")
}


tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        dependsOn(named("generateGResource"))
    }
}
