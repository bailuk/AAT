-ignorewarnings
-injars  build/libs/aat-gtk-all.jar
-outjars build/libs/aat-gtk-all-pro.jar

# Tis option gets set in `build.gradle.kts`
# -libraryjars /usr/lib/jvm/java-17-openjdk-amd64/jmods/java.base.jmod

# Kotlin
# -keep class kotlin.Metadata { *; }

# Preserve all annotations.
-keepattributes *Annotation*

# Entry point to the app.
-keep class ch.bailu.aat_gtk.app.AppKt { *; }

# H2 Driver (registers during runtime and JNA)
-keep class org.h2.Driver { *; }
-keep class org.h2.jdbc.** { *; }

# For debuging: to log class names
-keepnames class * { *; }

# XML Pull parser factory (loads class during runtime)
-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }

# JNA: to prevent link error
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
