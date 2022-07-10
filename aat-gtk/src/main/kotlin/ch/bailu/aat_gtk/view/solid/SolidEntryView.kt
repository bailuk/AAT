package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.view.util.setTxt
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class SolidEntryView (private val solid: AbsSolidType) : OnPreferencesChanged{
    val layout = Box(Orientation.VERTICAL, 5)
    val label = Label(Str.NULL)

    private val entry = Entry()
    private val editable = Editable(entry.cast())

    init {
        label.setTxt(solid.label)
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
