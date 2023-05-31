package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Button

class SolidImageButton (val solid: SolidIndexList) : OnPreferencesChanged, Attachable {

    val button = Button()

    init {
        button.setIconName(solid.iconResource)

        button.onClicked {
            solid.cycle()
        }

        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            button.setIconName(solid.iconResource)
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
