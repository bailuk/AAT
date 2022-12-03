package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.type.Str

class SolidIndexMenu(private val solid: SolidIndexList) : MenuProvider {

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder().custom(solid.key)
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    var group: CheckButton? = null

                    selectionMode = 0
                    solid.stringArray.forEachIndexed { index, it ->
                        append(CheckButton().apply {
                            label = Str(it)

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
            )
        )
    }
}
