package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Button

class SolidImageButton (val solid: SolidIndexList) : OnPreferencesChanged, Attachable {

    val button: Button = Button()

    companion object {
        const val ICON_SIZE = 25
    }


    init {
        button.image = IconMap.getImage(solid.iconResource, ICON_SIZE)
        button.onClicked {
            solid.cycle()
        }

        solid.register(this)
    }


    override fun onPreferencesChanged(storage: StorageInterface?, key: String?) {
        if (key != null && solid.hasKey(key)) {
            button.image = IconMap.getImage(solid.iconResource, ICON_SIZE)
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