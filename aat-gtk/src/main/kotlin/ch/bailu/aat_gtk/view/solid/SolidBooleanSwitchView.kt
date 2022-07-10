package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.util.setTxt
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Switch
import ch.bailu.gtk.type.Str

class SolidBooleanSwitchView(private val solid: SolidBoolean) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = Label(Str.NULL)

    private val toggleBox = Box(Orientation.HORIZONTAL, 0)
    private val toggle = Switch()


    init {
        label.setTxt(solid.label)
        label.xalign = 0f
        layout.append(label)
        layout.append(toggleBox)
        toggleBox.append(toggle)
        toggle.hexpand = GTK.FALSE
        toggle.active = GTK.IS(solid.value)

        solid.storage.register(this)

        toggle.onStateSet {
            solid.value = GTK.IS(it)
            GTK.TRUE
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            toggle.state = GTK.IS(solid.value)
        }
    }
}
