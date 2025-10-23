package ch.bailu.aat_lib.api

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException

abstract class ApiConfiguration : Api() {

    @Throws(UnsupportedEncodingException::class)
    abstract fun getUrl(query: String, bounding: BoundingBoxE6): String

    abstract val urlStart: String
    abstract val baseDirectory: Foc
    abstract val fileExtension: String
    abstract fun getUrlPreview(query: String, bounding: BoundingBoxE6): String

    abstract fun startTask(appContext: AppContext, boundingBoxE6: BoundingBoxE6)

    val queryFile: Foc
        get() = baseDirectory.child("query.txt")

    companion object {
        private const val NAME_MAX = 15
        private const val NAME_MIN = 2

        fun getFilePrefix(query: String): String {
            val name = StringBuilder()
            var i = 0
            while (i < query.length && name.length < NAME_MAX) {
                appendToName(query[i], name)
                i++
            }
            if (name.length < NAME_MIN) {
                name.append(AppDirectory.generateDatePrefix())
            }
            return name.toString()
        }

        private fun appendToName(character: Char, name: StringBuilder) {
            if (Character.isLetter(character)) {
                name.append(character)
            }
        }
    }
}
