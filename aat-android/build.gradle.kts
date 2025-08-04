import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ch.bailu.aat"
    compileSdk = 33

    buildFeatures.buildConfig = true

    /* Set this to whatever version is installed on the GitHub build environment
    and hope it works everywhere.
    ndkVersion "21.1.6352462" */
    ndkVersion="21.3.6528147"

    val appVersionName : String by project
    val appName : String by project
    val appId : String by project

    defaultConfig {
        minSdk = 22
        targetSdk = 33

        // Version Code can not be taken from from variable (f-droid version checker fail)
        versionCode = 42
        versionName = appVersionName
        applicationId = appId
    }

    lint {
        abortOnError = true
        checkDependencies = true
    }



    buildTypes {
        getByName("release") {
            resValue("string", "app_sname", appName)
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }

        getByName("debug") {
            resValue("string", "app_sname", "$appName Debug")
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
        }
    }


    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}


dependencies {
    // Local
    implementation(project(":aat-lib"))

    /**
     *    https://github.com/bailuk/foc
     */
    val focVersion: String by project
    implementation("com.github.bailuk.foc:foc-android:$focVersion")

    /*
            Automatic MapsForge integration as described here:
            https://github.com/mapsforge/mapsforge/raw/master/docs/Integration.md
    */
    val mapsForgeVersion: String by project


    // Android support only Junit 4
    // https://mvnrepository.com/artifact/junit/junit
    testImplementation("junit:junit:4.12")

    // MapsForge Android
    implementation("org.mapsforge:mapsforge-map-android:$mapsForgeVersion")
    implementation("com.caverock:androidsvg:1.4")

    // MapsForge POI
    implementation("org.mapsforge:mapsforge-poi-android:$mapsForgeVersion")

    // Acra
    val acraVersion: String by project
    implementation("ch.acra:acra-mail:$acraVersion")
    implementation("ch.acra:acra-dialog:$acraVersion")
}


tasks {
    withType(AbstractCompile::class) {
        dependsOn(":ci:generateImageMapping")
    }
}
