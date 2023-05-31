package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Widget


interface MenuProvider {
    fun createMenu() : Menu
    fun createCustomWidgets() : Array<CustomWidget>
    fun createActions(app: Application)
}

// TODO change to abstract class / interface?
data class CustomWidget(val widget: Widget, val id: String, val update: ()->Unit)
