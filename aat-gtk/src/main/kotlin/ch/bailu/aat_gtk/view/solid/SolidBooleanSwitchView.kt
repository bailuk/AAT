package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.util.GtkLabel
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Switch

class SolidBooleanSwitchView(private val solid: SolidBoolean) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = GtkLabel()

    private val toggleBox = Box(Orientation.HORIZONTAL, 0)
    private val toggle = Switch()


    init {
        label.text = solid.label
        label.xalign = 0f
        layout.append(label)
        layout.append(toggleBox)
        toggleBox.append(toggle)
        toggle.hexpand = GTK.FALSE
        toggle.active = GTK.IS(solid.value)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            toggle.active = GTK.IS(solid.value)
        }
    }
}
