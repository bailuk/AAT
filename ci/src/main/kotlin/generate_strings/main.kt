package generate_strings

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.BufferedWriter
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

private const val HEADER = """package ch.bailu.aat_lib.resources.generated
/**
    This file was generated by 'ci/src/main/kotlin/generate_strings/main.kt'
    Modify stings in 'aat-android/src/main/res/...'
*/
"""

fun main() {
    write()
    write("de")
    write("fr")
    write("nl")
    write("cs")
}


private fun write() {
    write(
        langPath = "values/",
        outFile = "Strings.kt",
        className = "open class Strings",
        funPrefix = "open")
}

private fun write(lang: String) {
    write(
        langPath = "values-${lang}/",
        outFile = "Strings_${lang}.kt",
        className = "class Strings_${lang} : Strings()",
        funPrefix= "override")
}

private fun write(langPath: String, outFile: String, className: String, funPrefix: String) {
    val inputFile = "aat-android/src/main/res/$langPath/strings.xml"
    val outputFile = "aat-lib/src/main/java/ch/bailu/aat_lib/resources/generated/$outFile"

    println("Generating $outputFile from $inputFile")

    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(inputFile))

    File(outputFile).bufferedWriter().use { out ->
        write(out, className, document, funPrefix)
    }
}


private fun write(out: BufferedWriter, className: String, document: Document, funPrefix: String) {
    out.write(HEADER)
    out.write("$className {\n")

    val root = document.documentElement

    val stringNodes = root.getElementsByTagName("string")
    for (i in 0 until stringNodes.length) {
        val tag = stringNodes.item(i) as Element
        val key = tag.getAttribute("name")
        val value = tag.textContent

        out.write("    $funPrefix fun $key(): String = \"$value\"\n\n")
    }

    val arrayNodes = root.getElementsByTagName("string-array")
    for (i in 0 until arrayNodes.length) {
        val arrayTag = arrayNodes.item(i) as Element
        out.write("    $funPrefix fun ${arrayTag.getAttribute("name")}(): Array<String> {\n")
        out.write("        return arrayOf(\n")

        val itemNodes = arrayTag.getElementsByTagName("item")
        for (j in 0 until itemNodes.length) {
            val item = itemNodes.item(j)
            out.write("            \"${item.textContent}\",\n")
        }

        out.write("        )\n")
        out.write("    }\n\n")
    }

    out.write("}")
}
