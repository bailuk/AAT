import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id ("java-library")
    id ("com.android.lint")
    id("org.jetbrains.kotlin.jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    val focVersion : String by project
    api("com.github.bailuk.foc:foc:$focVersion")
    testImplementation("com.github.bailuk.foc:foc-extended:$focVersion")

    val mapsForgeVersion: String by project

    // MapsForge Core
    implementation("org.mapsforge:mapsforge-core:$mapsForgeVersion")
    implementation("org.mapsforge:mapsforge-map:$mapsForgeVersion")
    implementation ("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    api ("org.mapsforge:mapsforge-themes:$mapsForgeVersion")

    api ("org.mapsforge:mapsforge-poi:$mapsForgeVersion")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.2")

    // Notnull annotation (still used in some Java classes)
    // https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs-annotations
    api("com.github.spotbugs:spotbugs-annotations:4.9.3")

    /**
     *  https://mvnrepository.com/artifact/net.sf.kxml/kxml2
     *  xml parser implementation
     */
    implementation("net.sf.kxml:kxml2:2.3.0")

    val jupiterVersion: String by project

    /**
     *   https://junit.org/junit5/docs/current/user-guide/#dependency-metadata
     *   https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
     */
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")

    /**
     * https://mvnrepository.com/artifact/com.google.openlocationcode/openlocationcode
     * https://github.com/google/open-location-code
     */
    implementation("com.google.openlocationcode:openlocationcode:1.0.4")

    /**
     *  https://mvnrepository.com/artifact/com.google.guava/guava
     *  For HtmlEscapers
     */
    implementation("com.google.guava:guava:33.5.0-jre")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

tasks {
    compileKotlin {
        dependsOn(":ci:property2config")
        dependsOn(":ci:generateStrings")
    }
    withType(AbstractCompile::class) {
        dependsOn(":ci:property2config")
        dependsOn(":ci:generateStrings")
    }
}
