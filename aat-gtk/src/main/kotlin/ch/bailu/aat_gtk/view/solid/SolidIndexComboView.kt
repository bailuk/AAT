package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str


class SolidIndexComboView(private val solid: SolidIndexList) : OnPreferencesChanged {
    val layout = Box(Orientation.VERTICAL, 5)
    val label = Label(Str.NULL)

    private val combo = ComboBoxText()

    init {
        label.setText(solid.getLabel())
        label.xalign = 0f

        layout.append(label)
        layout.append(combo)

        val list = solid.stringArray

        for (index in 0 until solid.length()) {
            combo.insertText(index, Str(list[index]))
        }
        combo.active = solid.index

        combo.onChanged {
            solid.index = combo.active
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            combo.active = solid.index
        }
    }
}
