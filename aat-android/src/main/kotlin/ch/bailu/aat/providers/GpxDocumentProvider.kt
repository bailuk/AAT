package ch.bailu.aat.providers

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.preferences.general.SolidPresetCount
import ch.bailu.aat_lib.preferences.presets.SolidMET
import ch.bailu.aat_lib.util.Objects
import ch.bailu.foc.Foc
import java.io.File
import java.io.FileNotFoundException
import java.util.Date

class GpxDocumentProvider : DocumentsProvider() {
    override fun queryRoots(projection: Array<String>?): Cursor {
        val result = MatrixCursor(projection ?: DEFAULT_ROOT_PROJECTION)
        result.newRow()
            .add(DocumentsContract.Root.COLUMN_ROOT_ID, ROOT)
            .add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.ic_launcher)
            .add(DocumentsContract.Root.COLUMN_FLAGS, DocumentsContract.Root.FLAG_SUPPORTS_RECENTS)
            .add(DocumentsContract.Root.COLUMN_TITLE, "AAT GPX")
            .add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, ROOT)
            .add(DocumentsContract.Root.COLUMN_MIME_TYPES, HashSet(listOf("application/gpx+xml")))
        return result
    }

    private fun includeFile(result: MatrixCursor, preset: Int, file: File) {
        result.newRow()
            .add(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                GPX_PREFIX + preset + GPX_INFIX + file.name
            )
            .add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.name)
            .add(DocumentsContract.Document.COLUMN_MIME_TYPE, "application/gpx+xml")
            .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            .add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
            .add(DocumentsContract.Document.COLUMN_SIZE, file.length())
    }

    private fun includeDirectory(result: MatrixCursor, preset: Int) {
        val presetLabel = context!!.getString(R.string.p_preset)
        val file = File(getTrackListDirectory(context, preset).path)
        val presetName = getPresetName(context, preset)
        result.newRow()
            .add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, DIR_PREFIX + preset)
            .add(
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                presetLabel + " " + (preset + 1) + ": " + presetName
            )
            .add(
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.MIME_TYPE_DIR
            )
            .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            .add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
            .add(DocumentsContract.Document.COLUMN_SIZE, file.length())
    }

    private fun getPresetName(context: Context?, preset: Int): String {
        return if (context != null) {
            SolidMET(Storage(context), preset).valueAsString
        } else ""
    }

    override fun queryDocument(documentId: String, projection: Array<String>?): Cursor {
        val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
        if (documentId == ROOT) {
            result.newRow()
                .add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, ROOT)
                .add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, "AAT GPX")
                .add(
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.MIME_TYPE_DIR
                )
                .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
        }
        if (documentId.startsWith(DIR_PREFIX)) {
            val preset = documentId.substring(DIR_PREFIX.length).toInt()
            includeDirectory(result, preset)
        }
        if (documentId.startsWith(GPX_PREFIX)) {
            val sep = documentId.indexOf(GPX_INFIX)
            val preset = documentId.substring(GPX_PREFIX.length, sep).toInt()
            val gpx = documentId.substring(sep + GPX_INFIX.length)
            val file = File(getTrackListDirectory(context, preset).descendant(gpx).path)
            includeFile(result, preset, file)
        }
        return result
    }

    override fun queryChildDocuments(
        parentDocumentId: String,
        projection: Array<String>?,
        sortOrder: String
    ): Cursor {
        val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
        if (parentDocumentId == ROOT) {
            val length = getPresetCount(context)
            for (i in 0 until length) {
                includeDirectory(result, i)
            }
            addRecentDocuments(result, length)
            return result
        }
        val preset = parentDocumentId.substring(DIR_PREFIX.length).toInt()
        val dir = File(getTrackListDirectory(context, preset).path)
        for (file in listFiles(dir)) {
            if (file.isFile && file.name.endsWith(".gpx")) {
                includeFile(result, preset, file)
            }
        }
        return result
    }

    private fun getPresetCount(context: Context?): Int {
        return if (context != null) {
            SolidPresetCount(Storage(context)).value
        } else {
            0
        }
    }

    @Throws(FileNotFoundException::class)
    override fun openDocument(
        documentId: String,
        mode: String,
        signal: CancellationSignal?
    ): ParcelFileDescriptor {
        if (!documentId.startsWith(GPX_PREFIX) || mode.contains("w")) throw FileNotFoundException(
            documentId
        )
        val sep = documentId.indexOf(GPX_INFIX)
        val preset = documentId.substring(GPX_PREFIX.length, sep).toInt()
        val gpx = documentId.substring(sep + GPX_INFIX.length)
        val context = context
        val file = File(getTrackListDirectory(context, preset).descendant(gpx).path)
        if (!file.isFile || !file.name.endsWith(".gpx")) throw FileNotFoundException(documentId)
        if (!Objects.equals(
                file.parentFile,
                File(getTrackListDirectory(context, preset).path)
            )
        ) {
            throw FileNotFoundException(documentId)
        }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.parseMode(mode))
    }

    override fun queryRecentDocuments(rootId: String, projection: Array<String>?): Cursor {
        val context = context
        val result = MatrixCursor(projection ?: DEFAULT_DOCUMENT_PROJECTION)
        val length = SolidPresetCount(Storage(context!!)).value
        addRecentDocuments(result, length)
        return result
    }

    private fun addRecentDocuments(result: MatrixCursor, numPresets: Int) {
        // Add gpx files from the last 24 hours
        val context = context
        val yesterday = Date().time - 86400000
        for (preset in 0 until numPresets) {
            val dir = File(getTrackListDirectory(context, preset).path)
            if (dir.exists() && dir.isDirectory) {
                for (file in listFiles(dir)) {
                    if (file.isFile && file.name.endsWith(".gpx") && file.lastModified() > yesterday) {
                        includeFile(result, preset, file)
                    }
                }
            }
        }
    }

    private fun listFiles(dir: File): Array<File> {
        return dir.listFiles() ?: return arrayOf()
    }

    private fun getTrackListDirectory(context: Context?, preset: Int): Foc {
        return context?.let { getTrackListDirectory(it, preset) } ?: Foc.FOC_NULL
    }

    override fun onCreate(): Boolean {
        return true
    }

    companion object {
        private val DEFAULT_ROOT_PROJECTION = arrayOf(
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_MIME_TYPES,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_ICON,
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_SUMMARY,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_AVAILABLE_BYTES
        )
        private val DEFAULT_DOCUMENT_PROJECTION = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_SIZE
        )
        private const val ROOT = "root"
        private const val DIR_PREFIX = "dir"
        private const val GPX_PREFIX = "gpx"
        private const val GPX_INFIX = "_"
    }
}
