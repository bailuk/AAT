package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gtk.ComboBoxText

class SolidDirectoryQueryComboView(appContext: AppContext)
    : OnPreferencesChanged {
    private val solidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private val directories = AppDirectory.getGpxDirectories(appContext)

    val combo = ComboBoxText()

    init {
        directories.forEachIndexed { index, gpxDirectoryEntry ->
            combo.insertText(index, gpxDirectoryEntry.name)
        }

        indexFromSolid()

        combo.onChanged {
            solidDirectoryQuery.setValue(directories[combo.active].file.path)
        }

        solidDirectoryQuery.register(this)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (key == solidDirectoryQuery.getKey()) {
            indexFromSolid()
        }
    }

    private fun indexFromSolid() {
        directories.forEachIndexed { index, directory ->
            if (directory.file == solidDirectoryQuery.getValueAsFile()) {
                combo.active = index
            }
        }
    }
}
