package ch.bailu.aat.util.fs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import ch.bailu.aat.util.Clipboard
import ch.bailu.aat.util.fs.FileIntent.send
import ch.bailu.aat.util.fs.FileIntent.view
import ch.bailu.aat.util.ui.AppDialog
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog
import ch.bailu.aat.views.preferences.dialog.AddOverlayDialog
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AFile
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc

object AndroidFileAction {

    fun delete(
        context: AppContext,
        activity: Activity,
        file: Foc
    ) {
        if (file.canWrite()) {
            object : AppDialog() {
                override fun onPositiveClick() {
                    file.rm()
                    FileAction.rescanDirectory(context, file)
                }
            }.displayYesNoDialog(
                activity,
                Res.str().file_delete_ask(),
                file.pathName
            )
        } else {
            AFile.logErrorReadOnly(file)
        }
    }

    fun copyToClipboard(context: Context, file: Foc) {
        copyToClipboard(file.name, file.toString(), context)
    }

    private fun copyToClipboard(label: String?, content: String?, context: Context) {
        if (label != null && content != null) Clipboard(context).setText(label, content)
    }

    fun useAsOverlay(context: Context, file: Foc) {
        AddOverlayDialog(context, file)
    }

    fun view(context: Context, uri: Uri) {
        view(context, Intent(), uri)
    }

    fun view(context: Context, file: Foc) {
        view(context, Intent(), file)
    }

    fun sendTo(context: Context, file: Foc) {
        send(context, Intent(), file)
    }

    fun copyToDir(context: Context, appContext: AppContext, src: Foc) {
        object : AppSelectDirectoryDialog(context, src) {
            override fun copyTo(
                context: Context,
                srcFile: Foc,
                destDirectory: Foc
            ) {
                try {
                    FileAction.copyToDir(appContext, srcFile, destDirectory)
                } catch (e: Exception) {
                    AppLog.e(appContext, e)
                }
            }
        }
    }

    fun rename(context: AppContext, activity: Activity, file: Foc) {

        if (file.canWrite() && file.hasParent()) {
            val directory = file.parent()
            val title = Res.str().file_rename() + " " + file.name
            val edit = EditText(activity)

            edit.setText(file.name)

            object : AppDialog() {
                override fun onPositiveClick() {
                    val source = directory.child(file.name)
                    val target = directory.child(edit.text.toString())
                    FileAction.rename(context, source, target)
                }
            }.displayTextDialog(activity, title, edit)

        } else {
            AFile.logErrorReadOnly(file)
        }
    }
}
