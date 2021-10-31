package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.LabelTextView
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.AbsSolidType
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

abstract class SolidLabelTextView(private val solid: AbsSolidType) : LabelTextView(solid.label), OnPreferencesChanged, Attachable {

    init {
        onAttached()

    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setValue()
        }
    }


    abstract fun onRequestNewValue()

    override fun onAttached() {
        setValue()
        solid.register(this)
    }

    override fun onDetached() {
        solid.unregister(this)
    }


    private fun setValue() {
        setValue("[$solid]")
    }
}
