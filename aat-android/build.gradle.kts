plugins {
    id("com.android.application")
}


android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    /* Set this to whatever version is installed on the GitHub build environment
    and hope it works everywhere.
    ndkVersion "21.1.6352462" */
    ndkVersion="21.3.6528147"

    defaultConfig {
        applicationId("ch.bailu.aat")

        minSdkVersion(14)
        targetSdkVersion(29)

        versionCode(34)
        versionName("v1.21")

    }

    lintOptions {
        isAbortOnError = true
        isCheckDependencies = true
    }



    buildTypes {
        getByName("release") {
            resValue("string", "app_sname", "AAT")
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }

        getByName("debug") {
            resValue("string", "app_sname", "AAT Debug")
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }


    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    useLibrary ("android.test.runner")

    useLibrary ("android.test.base")
    useLibrary ("android.test.mock")
}


repositories {
    google()
    mavenCentral()
    mavenLocal()
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

    // MapsForge Core
    implementation ("org.mapsforge:mapsforge-map-reader:$mapsForgeVersion")
    implementation ("org.mapsforge:mapsforge-themes:$mapsForgeVersion")

    // MapsForge Android
    implementation ("org.mapsforge:mapsforge-map-android:$mapsForgeVersion")
    implementation ("com.caverock:androidsvg:1.4")

    // MapsForge POI
    implementation ("org.mapsforge:mapsforge-poi:$mapsForgeVersion")
    implementation ("org.mapsforge:mapsforge-poi-android:$mapsForgeVersion")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion")

    // SqLite natives needed by MapsForge POI (when using remote repository)
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-armeabi-v7a")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-arm64-v8a")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-x86")
    implementation ("org.mapsforge:sqlite-android:$mapsForgeVersion:natives-x86_64")

    // Acra
    val acraVersion = "5.5.0"
    implementation ("ch.acra:acra-mail:$acraVersion")
    implementation ("ch.acra:acra-dialog:$acraVersion")


    // test
    // Required -- JUnit 4 framework
    testImplementation ("junit:junit:4.12")
    // Optional -- Robolectric environment
    testImplementation("androidx.test:core:1.4.0")
    // Optional -- Mockito framework
    testImplementation ("org.mockito:mockito-core:1.10.19")

}
