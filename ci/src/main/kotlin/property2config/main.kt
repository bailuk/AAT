package property2config

import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.util.*

fun main() {
    try {
        generate(
            File("gradle.properties"),
            File("aat-lib/src/main/java/ch/bailu/aat_lib/Configuration.kt"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun generate(propertyFile: File, kotlinFile: File) {
    val properties = Properties().apply { load(FileInputStream(propertyFile)) }

    FileWriter(kotlinFile).apply {
        append("package ${fileToPackage(kotlinFile)}\n\n")
        append("// Generated from '${propertyFile.name}' with 'property2config'\n")
        append("object ").append(fileToClass(kotlinFile)).append(" {\n")

        for (key in properties.keys) {
            val attribute = key.toString()
            if (!attribute.contains(".")) {
                append("    const val $key = \"${properties[key].toString()}\"\n")
            }
        }
        append("}\n")
        close()
    }
}

private fun fileToClass(file: File): String {
    val name = file.name
    val split = name.split(".").toTypedArray()
    return if (split.size == 2) {
        split[0]
    } else {
        throw Exception("Invalid kotlin file name: $name")
    }
}

private fun fileToPackage(file: File): String {
    val dirs = ArrayList<String>()

    var parent = file.parentFile
    while (parent != null && "java" != parent.name) {
        dirs.add(parent.name)
        parent = parent.parentFile
    }
    return dirsToPackage(dirs)
}

private fun dirsToPackage(dirs: List<String>): String {
    val result = StringBuilder("")
    var del = ""

    for (i in dirs.size - 1 downTo 0) {
        result.append(del).append(dirs[i])
        del = "."
    }
    return result.toString()
}
