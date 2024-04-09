package ch.bailu.aat.util.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat.R
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

abstract class AppSelectDirectoryDialog(appContext: AppContext, context: Context, private val srcFile: Foc) {
    private val directories = AppDirectory.getGpxDirectories(appContext)

    init {
        AlertDialog.Builder(context).apply {
            setTitle(srcFile.name + ": " + context.getString(R.string.file_copy))

            setItems(toNameArray(directories)) { dialogInterface: DialogInterface, index: Int ->
                copyTo(context, srcFile, directories[index].file)
                dialogInterface.dismiss()
            }

            create()
            show()
        }
    }

    private fun toNameArray(directories: ArrayList<AppDirectory.GpxDirectoryEntry>): Array<String> {
        return Array(directories.size) {
            directories[it].name
        }
    }

    abstract fun copyTo(context: Context, srcFile: Foc, destDirectory: Foc)
}
