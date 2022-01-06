plugins {
    application

    // https://kotlinlang.org/docs/gradle.html#targeting-the-jvm
    kotlin("jvm") version "1.6.10"
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    mavenLocal()
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
    //implementation("ch.bailu:foc:$focVersion")

    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    // same version as in mapsforge-poi-awt
    //implementation("org.xerial:sqlite-jdbc:3.28.0")

    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.0.204")

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
    implementation("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-themes:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-map-gtk:master-SNAPSHOT")

    /**
     *
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     *
     */
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

val appMainClass = "ch.bailu.aat_gtk.app.AppKt"

application {
    mainClass.set(appMainClass)
}


/**
 * https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html
 * https://stackoverflow.com/questions/41794914/how-to-create-the-fat-jar-with-gradle-kotlin-script
*/
val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = appMainClass
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}


