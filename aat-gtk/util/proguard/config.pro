#
# This ProGuard configuration file illustrates how to process applications.
# Usage:
#     java -jar proguard.jar @applications.pro
#
# From: https://github.com/Guardsquare/proguard/blob/master/examples/standalone/applications.pro

-verbose

# Specify the input jars, output jars, and library jars.

-injars  in.jar
-outjars out.jar

# Before Java 9, the runtime classes were packaged in a single jar file.
#-libraryjars <java.home>/lib/rt.jar

# As of Java 9, the runtime classes are packaged in modular jmod files.
-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
#-libraryjars <java.home>/jmods/.....

#-libraryjars junit.jar
#-libraryjars servlet.jar
#-libraryjars jai_core.jar
#...

# Save the obfuscation mapping to a file, so you can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-printmapping out.map
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Preserve all annotations.

-keepattributes *Annotation*

# You can print out the seeds that are matching the keep options below.

#-printseeds out.seeds

# Preserve all public applications.

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Preserve all native method names and the names of their classes.

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# Preserve the special static methods that are required in all enumeration
# classes.

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your application doesn't use serialization.
# If your code contains serializable classes that have to be backward
# compatible, please refer to the manual.

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Your application may contain more items that need to be preserved;
# typically classes that are dynamically created using Class.forName:

# -keep public class com.example.MyClass
# -keep public interface com.example.MyInterface
# -keep public class * implements com.example.MyInterface
