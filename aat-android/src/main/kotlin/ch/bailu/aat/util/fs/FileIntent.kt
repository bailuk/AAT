package ch.bailu.aat.util.fs

import android.content.Context
import android.content.Intent
import android.net.Uri
import ch.bailu.foc.Foc

object FileIntent {

    fun view(context: Context, intent: Intent, file: Foc) {
        if (file.canRead()) {
            intent.action = Intent.ACTION_VIEW
            if (file.isDir || file.path.startsWith("content:/")) {
                intent.data = Uri.parse(file.path)
            } else {
                intent.data = toContentUri(file)
            }
            context.startActivity(Intent.createChooser(intent, file.name))
        }
    }

    fun view(context: Context, intent: Intent, uri: Uri) {
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        context.startActivity(Intent.createChooser(intent, uri.lastPathSegment))
    }

    private fun toContentUri(file: Foc): Uri {
        return Uri.parse("content://ch.bailu.aat.gpx$file")
    }

    fun send(context: Context, intent: Intent, file: Foc) {
        /*
          This is the correct implementation for sending one file as an e-mail attachment.
          It does, however, not work with private files.

         */
        val uri = toContentUri(file)
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, file.name)
        intent.putExtra(Intent.EXTRA_TEXT, file.toString())
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        setType(intent, file) // only works with type setCopy (gmail and android mail)
        context.startActivity(Intent.createChooser(intent, file.name))
    }

    private fun setType(intent: Intent, file: Foc) {
        val type = mimeTypeFromFileName(file.name)
        if (type != null) {
            intent.type = type
        }
    }

    private fun mimeTypeFromFileName(name: String): String? {
        if (name.endsWith(".gpx")) return "application/gpx+xml" else if (name.endsWith(".osm")) return "application/xml"
        return null
    }
}
