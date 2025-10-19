package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.util.Directory
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.util.extensions.toPathString
import ch.bailu.aat_gtk.view.dialog.FileDialogBuilder
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.extensions.ellipsizeStart
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
                    solid.buildSelection().apply {
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
            val builder =  FileDialogBuilder()
                .path(solid.getValueAsFile())
                .title(solid.getLabel())

            if (solid.isDirectory()) {
                builder.selectFolder(window) {
                    solid.setValueFromString(it.path)
                }
            } else {
                builder.addPatters(solid.getPatterns())
                builder.open(window) {
                    solid.setValueFromString(it.path)
                }
            }
        }

        MenuHelper.setAction(app, "open${solid.getKey()}") {
            val pathString = solid.getValueAsFile().toPathString()
            if (pathString.isNotEmpty()) {
                Directory.openExternal(pathString)
            }
        }
    }

    override fun updateActionValues(app: Application) {}

}
