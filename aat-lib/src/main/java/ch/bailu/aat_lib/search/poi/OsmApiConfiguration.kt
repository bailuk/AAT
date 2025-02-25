package ch.bailu.aat_lib.search.poi

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException

abstract class OsmApiConfiguration {
    abstract val apiName: String

    @Throws(UnsupportedEncodingException::class)
    abstract fun getUrl(query: String): String

    abstract val urlStart: String
    abstract val baseDirectory: Foc
    abstract val fileExtension: String
    abstract fun getUrlPreview(query: String): String

    abstract fun startTask(appContext: AppContext)

    abstract val resultFile: Foc

    val queryFile: Foc
        get() = baseDirectory.child("query.txt")

    fun isTaskRunning(scontext: ServicesInterface): Boolean {
        var running = false
        scontext.insideContext {
            val background = scontext.getBackgroundService()
            running = background.findTask(resultFile) != null
        }
        return running
    }

    fun stopTask(scontext: ServicesInterface) {
        scontext.insideContext {
            val background = scontext.getBackgroundService()
            val task: FileTask? = background.findTask(resultFile)
            task?.stopProcessing()
        }
    }

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
