package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.util.fs.AndroidFileAction
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc

abstract class AbsFileMenu(private val aContext: ActivityContext, protected val file: Foc): AbsMenu() {
    override val title: String
        get() = file.name

    protected fun inflateCopyToSelect(menu: Menu) {
        add(menu, R.string.file_copy) {
            AndroidFileAction.copyToDir(
                aContext,
                aContext.appContext,
                file
            )
        }
    }

    protected fun inflateCopyTo(menu: Menu) {
        add(menu, Res.str().edit_save_copy()) {
            val targetDir = AppDirectory.getDataDirectory(
                aContext.appContext.dataDirectory,
                AppDirectory.DIR_OVERLAY
            )
            FileAction.copyToDir(aContext.appContext, file, targetDir)
        }
    }

    protected fun inflateManipulate(menu: Menu) {
        add(menu, R.string.file_view) { AndroidFileAction.view(aContext, file) }
        add(menu, R.string.file_rename) {
            AndroidFileAction.rename(
                aContext.appContext,
                aContext,
                file
            )
        }
        add(menu, R.string.file_delete) {
            AndroidFileAction.delete(
                aContext.appContext,
                aContext,
                file
            )
        }
        add(menu, R.string.file_reload) { FileAction.reloadPreview(aContext.appContext, file) }
        add(menu, R.string.file_mock) { FileAction.useForMockLocation(aContext.appContext, file) }
    }

    protected fun inflateOverlay(menu: Menu) {
        add(menu, R.string.file_overlay) { AndroidFileAction.useAsOverlay(aContext.appContext, aContext, file) }
    }

    protected fun inflateCopySend(menu: Menu) {
        add(menu, R.string.clipboard_copy) { AndroidFileAction.copyToClipboard(aContext, file) }
        add(menu, R.string.file_send) { AndroidFileAction.sendTo(aContext, file) }
    }

    override fun prepare(menu: Menu) {}
}
