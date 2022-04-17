package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_lib.preferences.SolidCheckList
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.CheckButton
import ch.bailu.gtk.gtk.ListBox
import ch.bailu.gtk.type.Str

class SolidCheckMenu(private val solid: SolidCheckList): MenuProvider {
    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder().custom(solid.key)
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf(
            CustomWidget(
                ListBox().apply {
                    selectionMode = 0
                    val enabledArray = solid.enabledArray

                    solid.stringArray.forEachIndexed { index, it ->
                        append(CheckButton().apply {
                            label = Str(it)
                            active = GTK.IS(enabledArray[index])
                            onToggled {
                                solid.setEnabled(index, GTK.IS(active))
                            }
                        })
                    }
                }, solid.key
            )
        )
    }
}
