package ch.bailu.aat.providers

import android.database.MatrixCursor
import android.provider.DocumentsContract
import ch.bailu.aat.BuildConfig
import ch.bailu.aat.R

class MatrixRoot(projection: Array<String>) {
    val matrix = MatrixCursor(projection)
    private val appTitle = if (BuildConfig.DEBUG) {
        "AAT Debug"
    } else {
        "AAT GPX"
    }

    fun newAppRoot() {
        matrix.newRow()
            .add(DocumentsContract.Root.COLUMN_ROOT_ID, Constants.ROOT)
            .add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.ic_launcher)
            .add(DocumentsContract.Root.COLUMN_FLAGS, DocumentsContract.Root.FLAG_SUPPORTS_RECENTS)
            .add(DocumentsContract.Root.COLUMN_TITLE, appTitle)
            .add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, Constants.ROOT)
            .add(DocumentsContract.Root.COLUMN_MIME_TYPES, HashSet(listOf(Constants.MIME_TYPE)))
    }

    fun newDirectoryRoot() {
        matrix.newRow()
            .add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, Constants.ROOT)
            .add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, appTitle)
            .add(DocumentsContract.Document.COLUMN_MIME_TYPE, DocumentsContract.Document.MIME_TYPE_DIR)
            .add(DocumentsContract.Document.COLUMN_FLAGS, 0)
    }
}
