package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gtk.DropDown
import ch.bailu.gtk.type.Strs


class SolidDirectoryDropDownView(appContext: AppContext)
    : OnPreferencesChanged {
    private val solidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private val directories = AppDirectory.getGpxDirectories(appContext)

    val dropDown: DropDown

    init {
        val model = ArrayList<String ?>()

        model.addAll(directories.map {
            limitWidth(it.name, 30) // TODO width limit should be handled by GTK (but how?)
        })
        model.add(null)
        dropDown = DropDown.newFromStringsDropDown(Strs(model.toTypedArray()))

        indexFromSolid()

        dropDown.onNotify {
            if ("selected" == it.name.toString()) { // Property "selected" has changed
                solidDirectoryQuery.setValue(directories[dropDown.selected].file.path)
            }
        }

        dropDown.showArrow = false
        solidDirectoryQuery.register(this)

        dropDown.onDestroy {
            solidDirectoryQuery.unregister(this)
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
            return text.substring(0, limit-2) + "…";
        }
        return text
    }
}
