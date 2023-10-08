package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.ComboBoxText
import ch.bailu.gtk.type.Str

class SolidDirectoryQueryComboView(storage: StorageInterface, focFactory: FocFactory)
    : OnPreferencesChanged {
    private val solid = SolidDirectoryQuery(storage, focFactory)
    private val sdirectory = SolidGtkDataDirectory(storage, focFactory)
    private val directories = AppDirectory.getGpxDirectories(sdirectory)

    val combo = ComboBoxText()

    init {
        var index = 0
        directories.forEach {
            combo.insertText(index++, Str(it.name))
        }
        indexFromSolid()
        combo.onChanged {
            solid.setValue(directories[combo.active].path)
        }
        solid.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (key == solid.getKey()) {
            indexFromSolid()
        }
    }

    private fun indexFromSolid() {
        var index = 0
        directories.forEach {
            if (it == solid.getValueAsFile()) {
                combo.active = index
            }
            index++
        }
    }
}
