package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.gtk.gtk.Widget


interface MenuProvider {
    fun createMenu() : MenuModelBuilder
    fun createCustomWidgets() : Array<CustomWidget>
}

data class CustomWidget(val widget: Widget, val id: String)
