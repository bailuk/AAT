package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.ListBox

class SolidIndexMenu(private val solid: SolidIndexList) : MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendItem(MenuHelper.createCustomItem(solid.key))
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    var group: CheckButton? = null

                    selectionMode = 0
                    solid.stringArray.forEachIndexed { index, it ->
                        append(CheckButton().apply {
                            setLabel(it)
                            if (group is CheckButton) {
                                setGroup(group)
                            } else {
                                group = this
                            }

                            active = solid.index == index
                            onToggled {
                                if (active) {
                                    solid.index = index
                                }
                            }
                        })
                    }
                }, solid.key
            ) {}
        )
    }

    override fun createActions(app: Application) {}
}
