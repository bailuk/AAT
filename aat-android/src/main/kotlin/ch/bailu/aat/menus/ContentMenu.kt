package ch.bailu.aat.menus

import android.view.Menu
import ch.bailu.aat.R
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.util.fs.AndroidFileAction
import ch.bailu.foc.Foc

class ContentMenu(private val context: ActivityContext, private val uri: Foc) : AbsMenu() {
    override fun inflate(menu: Menu) {
        add(menu, R.string.file_send)  { AndroidFileAction.sendTo(context, uri) }

        add(menu, R.string.file_view) { AndroidFileAction.view(context, uri) }
        add(menu, R.string.file_copy) { AndroidFileAction.copyToDir(context, context.appContext, uri) }
        add(menu, R.string.clipboard_copy) { AndroidFileAction.copyToClipboard(context, uri) }
    }

    override val title: String
        get() = uri.name

    override fun prepare(menu: Menu) {}
}
