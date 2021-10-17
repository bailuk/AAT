plugins {
    application
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}


tasks.test {
    useJUnitPlatform()
}


dependencies {

    implementation(project(":aat-lib"))

     /**
     *    https://mvnrepository.com/artifact/com.github.hypfvieh/dbus-java
     *    https://github.com/hypfvieh/dbus-java
     */
    implementation("com.github.hypfvieh:dbus-java:3.3.0")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")


    /**
     *    https://github.com/mapsforge/mapsforge/blob/master/docs/Integration.md
     */
    val mapsForgeVersion: String by project

    // MapsForge Core
    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-themes:$mapsForgeVersion")


    // AWT
    implementation("org.mapsforge:mapsforge-map-awt:$mapsForgeVersion")
    implementation("com.formdev:svgSalamander:1.1.2.4")


    /**
     *
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     *
      */
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")

}


application {
   mainClass.set("ch.bailu.aat_awt.app.App")
}


/**
 * https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html
 *//*

jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier = "all"

    manifest {
        attributes "Main-Class": "ch.bailu.aat_awt.app.App"
    }

    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}*/
