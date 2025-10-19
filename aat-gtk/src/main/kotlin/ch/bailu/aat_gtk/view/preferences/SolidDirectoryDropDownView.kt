package ch.bailu.aat_gtk.view.preferences

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gtk.DropDown
import ch.bailu.gtk.type.Strs


class SolidDirectoryDropDownView(appContext: AppContext)
    : OnPreferencesChanged {
    private val solidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private val directories = AppDirectory.getGpxDirectories(appContext)

    val dropDown: DropDown

    init {
        dropDown = DropDown.newFromStringsDropDown(createModel())

        indexFromSolid()

        dropDown.onNotify {
            if ("selected" == it.name.toString()) { // Property "selected" has changed
                solidDirectoryQuery.setValue(directories[dropDown.selected].file.path)
            }
        }

        dropDown.showArrow = false
        solidDirectoryQuery.register(this)

        dropDown.onDestroy {
            dropDown.disconnectSignals()
            solidDirectoryQuery.unregister(this)
            // TODO free model
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (key == solidDirectoryQuery.getKey()) {
            indexFromSolid()
        }
    }

    private fun indexFromSolid() {
        directories.forEachIndexed { index, directory ->
            if (directory.file == solidDirectoryQuery.getValueAsFile()) {
                dropDown.selected = index
            }
        }
    }

    private fun limitWidth(text: String, limit: Int): String {
        if (text.length > limit) {
            return text.substring(0, limit-2) + "…"
        }
        return text
    }

    private fun createModel(): Strs {
        val array = directories.map {
            limitWidth(it.name, 40) // TODO width limit should be handled by GTK (but how?)
        }.toList().toTypedArray()
        return Strs.nullTerminated(array)
    }
}
