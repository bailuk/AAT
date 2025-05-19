package ch.bailu.aat.util.fs

import android.content.Context
import android.content.Intent
import android.net.Uri
import ch.bailu.aat.preferences.SolidExportedDocument
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

object FileIntent {

    fun view(context: Context, intent: Intent, file: Foc) {
        if (file.canRead() && file.isFile) {
            intent.action = Intent.ACTION_VIEW
            intent.data = toExportedUri(context, file)
            context.startActivity(Intent.createChooser(intent, file.name))
        }
    }

    private fun toExportedUri(context: Context, file: Foc): Uri {
        SolidExportedDocument(Storage(context)).setDocument(file)
        val result = if (file.path.startsWith("content:/")) {
            Uri.parse(file.path)
        } else {
            val url = "content://${AppConfig.getInstance().appId}.gpx$file"
            AppLog.d(this, url)
            Uri.parse(url)
        }
        AppLog.d(this, result.path)
        return result
    }

    fun view(context: Context, intent: Intent, uri: Uri) {
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        context.startActivity(Intent.createChooser(intent, uri.lastPathSegment))
    }

    fun send(context: Context, intent: Intent, file: Foc) {
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, file.name)
        intent.putExtra(Intent.EXTRA_TEXT, file.toString())
        intent.putExtra(Intent.EXTRA_STREAM, toExportedUri(context, file))
        intent.setType(AppDirectory.getMimeTypeFromFileName(file.name))
        context.startActivity(Intent.createChooser(intent, file.name))
    }

}
