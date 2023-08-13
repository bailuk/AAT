package ch.bailu.aat.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.File
import java.io.FileNotFoundException

class GpxProvider : ContentProvider() {
    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val path = uri.path
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            }
        }
        throw UNSUPPORTED
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings2: Array<String>?,
        s2: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE))
        val path = uri.path

        if (path != null) {
            val file = File(path)
            cursor.addRow(arrayOf<Any>(file.name, file.length()))
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        val path = uri.path
        return if (path != null) {
            mimeTypeFromFileName(File(path).name)
        } else {
            null
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        throw UNSUPPORTED
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        throw UNSUPPORTED
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        throw UNSUPPORTED
    }

    companion object {
        private val UNSUPPORTED = UnsupportedOperationException("Not supported by this provider")
        fun mimeTypeFromFileName(name: String): String? {
            if (name.endsWith(".gpx")) return "application/gpx+xml" else if (name.endsWith(".osm")) return "application/xml"
            return null
        }
    }
}
