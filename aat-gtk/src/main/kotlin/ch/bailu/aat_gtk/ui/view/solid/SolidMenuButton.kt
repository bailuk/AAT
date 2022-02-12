package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.menu.PopupButton
import ch.bailu.aat_gtk.ui.view.menu.SolidIndexMenu
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.helper.ActionHelper

class SolidMenuButton(actionHelper: ActionHelper, private val solid: SolidIndexList)
    : PopupButton(actionHelper, SolidIndexMenu(solid)), OnPreferencesChanged, Attachable {

    init {
        solid.register(this)
        setIcon(solid.iconResource)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setIcon(solid.iconResource)
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
