package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc

class ResultFileMenu(
    private val context: ActivityContext,
    file: Foc,
    private val targetPrefix: String,
    private val targetExtension: String
) : FileMenu(
    context, file
) {

    override fun inflateCopyTo(menu: Menu) {
        add(menu, Res.str().edit_save_copy()) {
            FileAction.copyToDir(
                context.appContext, file,
                AppDirectory.getDataDirectory(
                    context.appContext.dataDirectory,
                    AppDirectory.DIR_OVERLAY
                ),
                targetPrefix, targetExtension
            )
        }
    }

    override fun inflateManipulate(menu: Menu) {}
}
