package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Widget


/**
 * Interface for popup- and application- menus that have a menu model,
 * optional custom widgets and
 * actions that react on menu actions and change values which back the menu model
 */
interface MenuProviderInterface :ActionProviderInterface {
    fun createMenu() : Menu
    fun createCustomWidgets() : Array<CustomWidget>
}

data class CustomWidget(val widget: Widget, val id: String, val update: ()->Unit)
