package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.menu.GtkMenu
import ch.bailu.aat_gtk.ui.view.menu.SolidIndexMenu
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gio.ActionMap
import ch.bailu.gtk.gtk.MenuButton

class SolidMenuButton(actionMap: ActionMap, private val solid: SolidIndexList): OnPreferencesChanged, Attachable {
    val button = MenuButton()

    companion object {
        const val ICON_SIZE = 25
    }

    init {
        //button. .child = IconMap.getImage(solid.iconResource, ICON_SIZE)
        button.menuModel = GtkMenu(actionMap, SolidIndexMenu(solid)).menu
        solid.register(this)
    }


    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            //button.image = IconMap.getImage(solid.iconResource, ICON_SIZE)
            AppLog.i(this, solid.valueAsString)
        }
    }

    override fun onAttached() {
        solid.register(this)
    }

    override fun onDetached() {
        solid.unregister(this)
    }

}