package ch.bailu.aat.util.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

abstract class AppSelectDirectoryDialog(private val context: Context, private val srcFile: Foc) {
    private val directories: Array<Foc>

    init {
        val sdirectory = SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context))
        directories = AppDirectory.getGpxDirectories(sdirectory)
        val names = arrayOfNulls<String>(directories.size)
        for (i in names.indices) names[i] = directories[i].name
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(srcFile.name + ": " + context.getString(R.string.file_copy))

        dialog.setItems(names) { dialogInterface: DialogInterface, index: Int ->
            copyTo(context, srcFile, directories[index])
            dialogInterface.dismiss()
        }

        dialog.create()
        dialog.show()
    }

    abstract fun copyTo(context: Context, srcFile: Foc, destDirectory: Foc)
}
