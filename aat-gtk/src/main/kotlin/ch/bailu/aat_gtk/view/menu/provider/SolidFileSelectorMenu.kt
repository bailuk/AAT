package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_lib.util.extensions.ellipsizeStart
import ch.bailu.aat_gtk.util.Directory
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.dialog.FileDialog
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class SolidFileSelectorMenu(private val solid: SolidFile, private val window: Window) :
    MenuProviderInterface {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(solid.getKey()))
            append("${Res.str().file_dialog()}…" , "app.get${solid.getKey()}")
            append("${Res.str().file_directory_open()}…", "app.open${solid.getKey()}")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    solid.buildSelection(ArrayList()).apply {
                        onRowActivated {
                            solid.setValue(this[it.index])
                        }
                    }.forEach {
                        val label = Label(Str.NULL)
                        label.setText(it.ellipsizeStart(30))
                        label.xalign = 0f
                        label.margin(3)
                        append(label)
                    }
                }, solid.getKey()
            ) {}
        )
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "get${solid.getKey()}") {
            FileDialog()
                .title(solid.getLabel())
                .path(pathFromFile(solid.getValueAsFile()))
                .onResponse {
                    if (it.isNotEmpty()) {
                        solid.setValueFromString(it)
                    }
                }.show(window)
            }
        MenuHelper.setAction(app, "open${solid.getKey()}") {
            Directory.openExternal(pathFromFile(solid.getValueAsFile()))
        }
    }

    override fun updateActionValues(app: Application) {}
    private fun pathFromFile(file: Foc): String {
        return if (file.isFile) {
            file.parent().path
        } else {
            file.path
        }
    }
}
