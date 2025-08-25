package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application

class LocationMenu : MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            append(Res.str().clipboard_copy(), MenuHelper.toAppAction(Strings.ACTION_LOCATION_COPY))
            append(Res.str().clipboard_paste(), MenuHelper.toAppAction(Strings.ACTION_LOCATION_PASTE))
            //append(Res.str().p_goto_location(), "app.locationGoto")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }


    override fun createActions(app: Application) {
    }

    companion object {
        fun createActions(storageInterface: StorageInterface, app: Application, display: Display, uiController: UiControllerInterface) {
            val clipboard = ClipboardController(display)

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_PASTE) {
                clipboard.getLatLong {
                    AppLog.i(this, it.toString())
                    uiController.centerInMap(it)
                }
            }

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_COPY) {
                val sgrid = SolidMapGrid(storageInterface, GtkCustomMapView.DEFAULT_KEY)
                val text = sgrid.getCode(uiController.getMapBounding().center.toLatLong()).toString()

                AppLog.i(this, text)
                clipboard.setText(sgrid.getCode(uiController.getMapBounding().center.toLatLong()).toString())
            }
        }
    }
}
