package ch.bailu.aat.menus

import android.view.Menu
import android.view.MenuItem
import ch.bailu.aat.views.description.mview.MultiView

class MultiViewMenu(private val mview: MultiView) : AbsMenu() {
    override fun inflate(menu: Menu) {
        mview.inflateMenu(menu)
    }

    override val title: String
        get() = ""

    override fun prepare(menu: Menu) {}
    override fun onItemClick(item: MenuItem): Boolean {
        mview.setActive(item.itemId)
        return true
    }
}
