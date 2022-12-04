package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application

class LocationMenu(private val app: Application) : MenuProvider {
    override fun createMenu(): Menu {
        return Menu().apply {
            append(Res.str().location_send(), "app.locationSend")
            append(Res.str().location_view(), "app.locationView")
            append(Res.str().clipboard_copy(), "app.locationCopy")
            append(Res.str().clipboard_paste(), "app.locationPaste")
            append(Res.str().p_goto_location(), "app.locationGoto")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }

    override fun createActions(app: Application) {}
}
