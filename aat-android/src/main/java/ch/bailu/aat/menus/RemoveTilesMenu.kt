package ch.bailu.aat.menus

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.tileremover.SelectedTileDirectoryInfo
import ch.bailu.aat.util.ui.AppDialog

class RemoveTilesMenu(private val scontext: ServiceContext, private val acontext: Activity) :
    AbsMenu() {
    private var removeScanned: MenuItem? = null
    private var removeAll: MenuItem? = null
    private val info: SelectedTileDirectoryInfo = scontext.getTileRemoverService().info

    override fun inflate(menu: Menu) {
        val c = scontext.context
        if (info.index == 0) {
            removeScanned = menu.add(c.getString(R.string.p_remove_old))
            removeAll = menu.add(c.getString(R.string.p_remove_all))
        } else {
            removeScanned = menu.add(c.getString(R.string.p_remove_old_in) + info.name)
            removeAll = menu.add(c.getString(R.string.p_remove_all_in) + info.name)
        }
    }

    override val title: String
        get() = info.name

    override fun prepare(menu: Menu) {}
    override fun onItemClick(item: MenuItem): Boolean {
        if (item === removeScanned) {
            scontext.insideContext { scontext.getTileRemoverService().state.remove() }
        } else if (item === removeAll) {
            object : AppDialog() {
                override fun onPositiveClick() {
                    scontext.insideContext { scontext.getTileRemoverService().state.removeAll() }
                }
            }.displayYesNoDialog(
                acontext,
                scontext.context.getString(R.string.p_remove_all),
                scontext.context.getString(R.string.p_remove_all_in) + " " + info.directory
            )
        }
        return false
    }
}
