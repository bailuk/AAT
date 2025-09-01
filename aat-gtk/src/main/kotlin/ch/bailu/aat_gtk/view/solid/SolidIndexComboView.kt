package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.gtk.DropDown
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs


class SolidIndexComboView(private val solid: SolidIndexList) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)
    val label = Label(Str.NULL)
    private var lockUpdate = false

    // TODO update name if changed in preferences
    private val combo = DropDown.newFromStringsDropDown(Strs.nullTerminated(solid.getStringArray()))

    init {
        label.setText(solid.getLabel())
        label.xalign = 0f

        layout.append(label)
        layout.append(combo)

        combo.selected = solid.index

        combo.onNotify {
            if ("selected".equals(it.name.toString())) {
                lockUpdate = true
                solid.index = combo.selected
                lockUpdate = false
            }
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (!lockUpdate && solid.hasKey(key)) {
            combo.selected = solid.index
        }
    }
}
