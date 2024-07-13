package ch.bailu.aat.providers

import android.database.MatrixCursor
import android.provider.DocumentsContract
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc

class MatrixDirectories(projection: Array<String>, private val context: ProviderContext) {
    val matrix = MatrixCursor(projection)

    fun includeDirectories() {
        for (i in 0 until context.getPresetCount()) {
            includeDirectory(i)
        }
    }

    fun includeDirectory(preset: Int) {
        val presetLabel = Res.str().p_preset()
        val directory = context.getTrackListDirectory(preset)
        val presetName = context.getPresetName(preset)

        matrix.newRow()
            .add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, Constants.DIR_PREFIX + preset)
            .add(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                "$presetLabel (${preset + 1}): $presetName"
            )
            .add(
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.MIME_TYPE_DIR
            )
            .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            .add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, directory.lastModified())
            .add(DocumentsContract.Document.COLUMN_SIZE, directory.length())
    }

    fun addRecentDocuments() {
        // Add gpx files from the last 24 hours
        val yesterday = System.currentTimeMillis() - 86400000 // 24*60*60*1000
        for (preset in 0 until context.getPresetCount()) {
            val directory = context.getTrackListDirectory(preset)
            if (directory.exists() && directory.isDir) {
                directory.foreachFile {
                    if (it.name.endsWith(".gpx") && it.lastModified() > yesterday) {
                        includeFile(it, preset)
                    }
                }
            }
        }
    }

    fun includeFiles(preset: Int) {
        val dir = context.getTrackListDirectory(preset)
        dir.foreachFile {
            if (it.name.endsWith(".gpx")) {
                includeFile(it, preset)
            }
        }
    }

    fun includeFile(file: Foc, preset: Int) {
        includeFile(file, preset.toString())
    }

    fun includeFile(file: Foc, preset: String = "") {
        matrix.newRow()
            .add(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                Constants.GPX_PREFIX + preset + Constants.GPX_INFIX + file.name
            )
            .add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.name)
            .add(DocumentsContract.Document.COLUMN_MIME_TYPE, Constants.MIME_TYPE)
            .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            .add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
            .add(DocumentsContract.Document.COLUMN_SIZE, file.length())
    }
}
