plugins {
    id("com.android.application")
    id ("kotlin-android")
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
        minSdk = 14
        targetSdk = 33
        versionCode = 37
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
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    useLibrary ("android.test.runner")

    useLibrary ("android.test.base")
    useLibrary ("android.test.mock")
}


dependencies {
    // Local
    implementation(project(":aat-lib"))

    /**
     *    https://github.com/bailuk/foc
     */
    val focVersion : String by project
    implementation("com.github.bailuk.foc:foc-android:$focVersion")
    //implementation("ch.bailu:foc:$focVersion")
    //implementation("ch.bailu:foc-android:$focVersion")



    /*
            Automatic MapsForge integration as described here:
            https://github.com/mapsforge/mapsforge/raw/master/docs/Integration.md
    */
    val mapsForgeVersion: String by project


    // MapsForge Android
    implementation ("org.mapsforge:mapsforge-map-android:$mapsForgeVersion")
    implementation ("com.caverock:androidsvg:1.4")

    // MapsForge POI
    implementation ("org.mapsforge:mapsforge-poi-android:$mapsForgeVersion")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion")

    // SqLite natives needed by MapsForge POI (when using remote repository)
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-armeabi-v7a")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-arm64-v8a")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-x86")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-x86_64")
    testImplementation("org.testng:testng:6.9.6")

    // Acra
    val acraVersion = "5.7.0"
    implementation ("ch.acra:acra-mail:$acraVersion")
    implementation ("ch.acra:acra-dialog:$acraVersion")


    // test
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    // Optional -- Robolectric environment
    testImplementation("androidx.test:core:1.5.0")
    // Optional -- Mockito framework
    // testImplementation ("org.mockito:mockito-core:1.10.19")

}
