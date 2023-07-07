package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Switch
import ch.bailu.gtk.type.Str

/**
 * TODO move to
 * [SwitchRow](https://gnome.pages.gitlab.gnome.org/libadwaita/doc/main/class.SwitchRow.html)
 * ADW 1.4
 */
class SolidBooleanSwitchView(private val solid: SolidBoolean) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)

    private val label = Label(Str.NULL)

    private val toggleBox = Box(Orientation.HORIZONTAL, 0)
    private val toggle = Switch()

    init {
        label.setText(solid.getLabel())
        label.xalign = 0f
        layout.append(label)
        layout.append(toggleBox)
        toggleBox.append(toggle)
        toggle.hexpand = false
        toggle.active = solid.value

        solid.storage.register(this)

        toggle.onStateSet {
            solid.value = it
            true
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            toggle.state = solid.value
        }
    }
}
