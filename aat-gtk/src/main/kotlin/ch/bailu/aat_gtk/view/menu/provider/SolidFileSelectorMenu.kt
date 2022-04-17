package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.FileDialog
import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_gtk.view.util.setText
import ch.bailu.aat_gtk.view.util.truncate
import ch.bailu.aat_lib.preferences.SolidFile
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class SolidFileSelectorMenu(private val solid: SolidFile, private val window: Window) :
    MenuProvider {

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .custom(solid.key)
            .label(ToDo.translate("File dialog...")) {
                FileDialog()
                    .label(solid.label)
                    .selectFolder()
                    .path(solid.valueAsString)
                    .response {
                        if (it.isNotEmpty()) {
                            solid.setValueFromString(it)
                        }
                    }
                    .show(window)
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
                        label.setText(it.truncate())
                        label.xalign = 0f
                        label.margin(3)
                        append(label)
                    }
                }, solid.key
            )
        )
    }
}
