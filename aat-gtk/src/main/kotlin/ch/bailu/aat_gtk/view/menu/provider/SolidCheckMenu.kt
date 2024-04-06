package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidCheckList
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.ListBox

class SolidCheckMenu(private val solid: SolidCheckList): MenuProvider {
    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(solid.getKey()))
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    selectionMode = 0
                    val enabledArray = solid.getEnabledArray()

                    solid.getStringArray().forEachIndexed { index, it ->
                        append(CheckButton().apply {
                            setLabel(it)
                            active = enabledArray[index]
                            onToggled {
                                solid.setEnabled(index, active)
                            }
                        })
                    }
                }, solid.getKey()
            ) {}
        )
    }

    override fun createActions(app: Application) {}
}
