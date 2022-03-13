package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.Label
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Editable
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class SolidEntryView (private val solid: AbsSolidType) : OnPreferencesChanged{
    val layout = Box(Orientation.HORIZONTAL, 4)
    val label = Label()

    val entry = Entry()
    val editable = Editable(entry.cast())

    init {
        label.text = solid.label
        layout.append(label)
        layout.append(entry)

        entry.buffer.setText(Str(solid.valueAsString),-1)

        editable.onChanged {
            solid.setValueFromString(entry.buffer.text.toString())
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            entry.buffer.setText(Str(solid.valueAsString),-1)
        }
    }


}