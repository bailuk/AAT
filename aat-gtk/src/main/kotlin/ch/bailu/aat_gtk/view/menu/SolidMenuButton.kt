package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.SolidIndexMenu
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidMenuButton(solid: SolidIndexList) : PopupMenuButtonOverlay(
    SolidIndexMenu(solid)
), OnPreferencesChanged {


    private val solid: AbsSolidType = solid

    init {
        this.solid.register(this)
        setIcon(this.solid.getIconResource())
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setIcon(solid.getIconResource())
            AppLog.i(this, solid.getValueAsString())
        }
    }
}
