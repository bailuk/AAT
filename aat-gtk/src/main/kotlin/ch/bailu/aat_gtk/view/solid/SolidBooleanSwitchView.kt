package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.util.extensions.setTooltipText
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.adw.SwitchRow

class SolidBooleanSwitchView(private val solid: SolidBoolean): OnPreferencesChanged {
    val layout = SwitchRow()
    private var blockUpdate = false

    init {
        layout.setTitle(solid.getLabel())
        layout.setTooltipText(solid)
        layout.active = solid.value
        layout.onNotify {
            if ("active" == it.name.toString()) {
                blockUpdate = true
                solid.value = layout.active
                blockUpdate = false
            }
        }
        layout.onDestroy {
            layout.disconnectSignals()
            solid.unregister(this)
        }
        solid.getStorage().register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!blockUpdate && solid.hasKey(key)) {
            layout.active = solid.value
        }
    }
}
