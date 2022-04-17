package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.util.GtkLabel
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Editable
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class SolidEntryView (private val solid: AbsSolidType) : OnPreferencesChanged{
    val layout = Box(Orientation.VERTICAL, 5)
    val label = GtkLabel()

    val entry = Entry()
    val editable = Editable(entry.cast())

    init {
        label.text = solid.label
        label.xalign = 0f

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
