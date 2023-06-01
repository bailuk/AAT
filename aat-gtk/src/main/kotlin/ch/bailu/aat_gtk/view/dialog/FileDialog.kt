package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gtk.FileChooser
import ch.bailu.gtk.gtk.FileChooserAction
import ch.bailu.gtk.gtk.FileChooserDialogExtended
import ch.bailu.gtk.gtk.FileFilter
import ch.bailu.gtk.gtk.ResponseType
import ch.bailu.gtk.gtk.Window

class FileDialog {
    private var title = ""
    private val cancel = Res.str().cancel()
    private val ok = Res.str().ok()

    private var initialPath = ""
    private var onResponseCallback = { _: String -> }

    private val filterMap: MutableMap<String, FileFilter> = HashMap()

    private var action = FileChooserAction.OPEN

    fun title(label: String): FileDialog {
        title = label
        return this
    }

    fun open(): FileDialog {
        action = FileChooserAction.OPEN
        return this
    }

    fun save(): FileDialog {
        action = FileChooserAction.SAVE
        return this
    }

    fun mimeType(name: String, mimeType: String?): FileDialog {
        if (!filterMap.containsKey(name)) {
            val filter = FileFilter()
            filter.setName(name)
            filterMap[name] = filter
        }
        filterMap[name]?.apply { addMimeType(mimeType) }
        return this
    }

    fun pattern(name: String, pattern: String?): FileDialog {
        if (!filterMap.containsKey(name)) {
            val filter = FileFilter()
            filter.setName(name)
            filterMap[name] = filter
        }
        filterMap[name]!!.addPattern(pattern)
        return this
    }

    fun onResponse(onResponse: (String)->Unit): FileDialog {
        onResponseCallback = onResponse
        return this
    }

    fun selectFolder(): FileDialog {
        action = FileChooserAction.SELECT_FOLDER
        return this
    }

    fun path(path: String): FileDialog {
        initialPath = path
        return this
    }

    fun show(window: Window) {
        val dialog = FileChooserDialogExtended(title, window, action)
        dialog.path = initialPath

        val chooser = FileChooser(dialog.cast())
        filterMap.keys.forEach { key: String ->
            filterMap[key]?.apply { chooser.addFilter(this) }
        }

        dialog.addButton(cancel, ResponseType.CANCEL)
        dialog.addButton(ok, ResponseType.OK)
        dialog.onDestroy {
            dialog.disconnectSignals()
        }

        dialog.onResponse { response: Int ->
            if (response == ResponseType.OK) {
                onResponseCallback(dialog.path)
            }
            dialog.close()
        }
        dialog.show()
    }
}
