package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.util.fs.AndroidFileAction
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc

open class FileMenu(private val activity: ActivityContext, protected val file: Foc) : AbsMenu() {

    override val title: String
        get() = file.name

    override fun inflate(menu: Menu) {
        add(menu, R.string.file_overlay) { AndroidFileAction.useAsOverlay(activity, file) }
        inflateCopyTo(menu)
        add(menu, R.string.clipboard_copy) { AndroidFileAction.copyToClipboard(activity, file) }
        add(menu, R.string.file_send) { AndroidFileAction.sendTo(activity, file) }
        inflateManipulate(menu)
    }

    protected open fun inflateCopyTo(menu: Menu) {
        add(menu, R.string.file_copy) {
            AndroidFileAction.copyToDir(
                activity,
                activity.appContext,
                file
            )
        }
    }

    protected open fun inflateManipulate(menu: Menu) {
        add(menu, R.string.file_view) { AndroidFileAction.view(activity, file) }
        add(menu, R.string.file_rename) {
            AndroidFileAction.rename(
                activity.appContext,
                activity,
                file
            )
        }
        add(menu, R.string.file_delete) {
            AndroidFileAction.delete(
                activity.appContext,
                activity,
                file
            )
        }
        add(menu, R.string.file_reload) { FileAction.reloadPreview(activity.appContext, file) }
        add(menu, R.string.file_mock) { FileAction.useForMockLocation(activity.appContext, file) }
    }

    override fun prepare(menu: Menu) {}
}
