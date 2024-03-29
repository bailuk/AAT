package generate_image_mapping

import java.io.File

fun main() {
    val inputFiles = listOf(
        "aat-android/build/intermediates/runtime_symbol_list/release/R.txt",
        "aat-android/build/intermediates/runtime_symbol_list/debug/R.txt"
    )
    val outputFile = "aat-android/src/main/kotlin/ch/bailu/aat/generated/Images.kt"
    val inputFile = inputFiles.firstOrNull { File(it).exists() }

    if (inputFile != null) {
        println("Generate '$outputFile' from '$inputFile'")
        generateImageResourceMapping(inputFile, outputFile)
    } else {
        println("No 'R.txt' found. Cannot generate image resource mapping")
    }
}

fun generateImageResourceMapping(inputFile: String, outputFile: String) {
    File(inputFile).bufferedReader().use { inReader ->
        File(outputFile).bufferedWriter().use { outWriter ->
            outWriter.write("/**\n")
            outWriter.write("    This file was generated by 'ci/src/main/kotlin/generate_image_mapping/main.kt'\n")
            outWriter.write("*/\n\n")
            outWriter.write("package ch.bailu.aat.generated;\n\n\n")
            outWriter.write("import ch.bailu.aat.R;\n\n")
            outWriter.write("object Images {\n\n")
            outWriter.write("    fun get(name: String): Int {\n")
            outWriter.write("        when (name) {\n")

            var count = 0
            inReader.forEachLine { line ->
                count++
                val words = line.split(" ")

                if (words.size > 3 && words[1] == "drawable") {
                    val name = words[2]
                    val number = words[3]

                    println("$count: $name, $number")

                    outWriter.write("            \"$name\" -> return R.drawable.$name\n")
                }
            }

            outWriter.write("        }\n")
            outWriter.write("        return 0\n")
            outWriter.write("    }\n")
            outWriter.write("}\n")
        }
    }
}
