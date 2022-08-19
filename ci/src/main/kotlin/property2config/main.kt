package property2config

import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.util.*


fun main(args: Array<String>) {
    try {
        generate(args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun generate(args: Array<String>) {
    if (args.size > 1) {
        generate(File(args[args.size - 2]), File(args[args.size - 1]))
    } else {
        throw Exception("Usage: property2config property-file-path java-file-path")
    }
}

private fun generate(propertyFile: File, javaFile: File) {
    val properties = Properties().apply { load(FileInputStream(propertyFile)) }

    FileWriter(javaFile).apply {
        append("package ${fileToPackage(javaFile)};\n\n")
        append("// Generated from '${propertyFile.name}' with 'property2config'\n")
        append("public class ").append(fileToClass(javaFile)).append("{\n")

        for (key in properties.keys) {
            val attribute = key.toString()
            if (!attribute.contains(".")) {
                append("    public final static String $key = \"${properties[key].toString()}\";\n")
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
        throw Exception("Invalid java file name: $name")
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
