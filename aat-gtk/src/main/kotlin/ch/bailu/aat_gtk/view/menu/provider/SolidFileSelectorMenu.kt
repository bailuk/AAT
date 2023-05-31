package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.FileDialog
import ch.bailu.aat_gtk.lib.extensions.ellipsizeStart
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class SolidFileSelectorMenu(private val solid: SolidFile, private val window: Window) :
    MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(solid.key))
            append(ToDo.translate("File dialog..."), "app.showSelectFileDialog${solid.key}")
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
                }, solid.key
            ) {}
        )
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "showSelectFileDialog${solid.key}") {
            FileDialog()
                .title(solid.label)
                .onResponse {
                    if (it.isNotEmpty()) {
                        solid.setValueFromString(it)
                    }
                }.show(window)
            }
    }
}
