package ch.bailu.aat.providers

import android.provider.DocumentsContract

object Constants {

    const val ROOT = "root"
    const val DIR_PREFIX = "dir"
    const val GPX_PREFIX = "gpx"
    const val GPX_INFIX = "_"

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
    val DEFAULT_DOCUMENT_PROJECTION = arrayOf(
        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
        DocumentsContract.Document.COLUMN_MIME_TYPE,
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        DocumentsContract.Document.COLUMN_FLAGS,
        DocumentsContract.Document.COLUMN_SIZE
    )

    fun getRootProjection(projection: Array<String>?): Array<String> {
        if (projection == null) {
            return DEFAULT_ROOT_PROJECTION
        }
        return projection
    }

    fun getDocumentProjection(projection: Array<String>?): Array<String> {
        if (projection == null) {
            return DEFAULT_DOCUMENT_PROJECTION
        }
        return projection
    }

}
