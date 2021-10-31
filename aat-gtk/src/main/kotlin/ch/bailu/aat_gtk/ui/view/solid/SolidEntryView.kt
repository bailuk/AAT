package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.Label
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Editable
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class SolidEntryView (private val solid: AbsSolidType) : OnPreferencesChanged{
    val layout = Box(Orientation.HORIZONTAL, 4)
    val label = Label()

    val entry = Entry()
    val editable = Editable(entry.cPointer)

    init {
        label.text = solid.label
        layout.packStart(label, GTK.FALSE, GTK.FALSE, 4)
        layout.packStart(entry, GTK.FALSE, GTK.FALSE, 4)

        entry.text = Str(solid.valueAsString)

        editable.onChanged {
            solid.setValueFromString(entry.text.toString())
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            entry.text = Str(solid.valueAsString)
        }
    }


}