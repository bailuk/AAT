package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Widget


interface MenuProviderInterface :ActionProviderInterface {
    fun createMenu() : Menu
    fun createCustomWidgets() : Array<CustomWidget>
}

data class CustomWidget(val widget: Widget, val id: String, val update: ()->Unit)
