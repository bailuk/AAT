plugins {
    id ("java-library")
    id ("com.android.lint")

    // https://kotlinlang.org/docs/gradle.html#targeting-the-jvm
    kotlin("jvm")
}


tasks.test {
    useJUnitPlatform()
}


java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    val focVersion : String by project
    api("com.github.bailuk.foc:foc:$focVersion")
    //api("ch.bailu:foc:$focVersion")

    val mapsForgeVersion: String by project

    // MapsForge Core
    implementation("org.mapsforge:mapsforge-core:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-map:$mapsForgeVersion")
    implementation ("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    api ("org.mapsforge:mapsforge-themes:$mapsForgeVersion")

    api ("org.mapsforge:mapsforge-poi:$mapsForgeVersion")

    /**
     *  Notnull annotation
     */
    api("com.google.code.findbugs:jsr305:3.0.2")

    /**
     *  https://mvnrepository.com/artifact/net.sf.kxml/kxml2
     *  xml parser implementation
     */
    implementation("net.sf.kxml:kxml2:2.3.0")

    /**
     *  https://mvnrepository.com/artifact/org.apache.commons/commons-text
     *  To escape html
     */
    implementation("org.apache.commons:commons-text:1.9")

    /**
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     */
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    /**
     * https://mvnrepository.com/artifact/com.google.openlocationcode/openlocationcode
     * https://github.com/google/open-location-code
     */
    implementation("com.google.openlocationcode:openlocationcode:1.0.4")
}


tasks {
    withType(AbstractCompile::class) {
        dependsOn(":ci:property2config")
        dependsOn(":ci:generateStrings")
    }
}
