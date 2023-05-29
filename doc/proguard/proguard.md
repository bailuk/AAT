# AAT-gtk and Proguard

## Advantage

Building a jar with Proguard saves about 10 MB:

- 23M 28. Mai 23:02 aat-gtk/build/libs/aat-gtk-all.jar
- 14M 28. Mai 23:02 aat-gtk/build/libs/aat-gtk-all-pro.jar

## Disadvantage

- Hard to setup and maintain (gradle and proguard)
- Missing Classes because (h2, kxml, jna) load classes dynamically using introspection (requires manual configuration)
- Long build time

## Conclusion

 Remove proguard from build process

 ## Configuration

 ```kotlin
// build.gradle.kts
buildscript {
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

```
