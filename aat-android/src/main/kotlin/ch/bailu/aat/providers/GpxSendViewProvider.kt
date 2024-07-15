package ch.bailu.aat.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.FocFile
import java.io.File
import java.io.FileNotFoundException

/**
 * To temporarily export private files to external applications
 * This is used from file context menu -> send to ... / view...
 */
class GpxSendViewProvider : ContentProvider() {
    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val context = this.context
        val path = uri.path

        if (context is Context && !mode.contains("w") && path is String) {
            val providerContext = ProviderContext(context)
            if (providerContext.isExportAllowed(path)) {
                val file = File(path)
                if (file.exists()) {
                    AppLog.d(this, "Export file: $path")
                    return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                }
            }
        }
        throw FileNotFoundException(path)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings2: Array<String>?,
        s2: String?
    ): Cursor {
        val context = this.context
        val path = uri.path

        if (context is Context && path is String) {
            val providerContext = ProviderContext(context)
            if (providerContext.isExportAllowed(path)) {
                val file = FocFile(File(path))
                if (file.exists()) {
                    AppLog.d(this, "Query exportable file: $path")
                    return MatrixDirectories(Constants.DEFAULT_DOCUMENT_PROJECTION, providerContext)
                        .includeFile(file)
                        .matrix
                }
            }
        }
        return MatrixCursor(Constants.DEFAULT_DOCUMENT_PROJECTION)
    }

    override fun getType(uri: Uri): String {
        val path = uri.path

        if (path is String) {
            return AppDirectory.getMimeTypeFromFileName(path)
        }
        return AppDirectory.getMimeTypeFromFileName("")
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
    }
}
