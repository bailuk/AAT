package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.api.NominatimReverseController
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.api.cm.CmApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str
import org.mapsforge.map.view.MapView

class LocationMenu : MenuProviderInterface {
    override fun createMenu(): Menu {
        return Menu().apply {
            append(Res.str().clipboard_copy(), MenuHelper.toAppAction(Strings.ACTION_LOCATION_COPY))
            append(
                Res.str().clipboard_paste(),
                MenuHelper.toAppAction(Strings.ACTION_LOCATION_PASTE)
            )
            appendSection(ToDo.translate("Reverse Nominatim"), Menu().apply {
                append(
                    ToDo.translate("Query"),
                    MenuHelper.toAppAction(Strings.ACTION_LOCATION_REVERSE)
                )
                append(
                    ToDo.translate("Center"),
                    MenuHelper.toAppAction(Strings.ACTION_LOCATION_REVERSE_CENTER)
                )
            })

            if (CmApi.ENABLED) {
                appendSection(Str.NULL, Menu().apply {
                    append(
                        ToDo.translate("CM locations"),
                        MenuHelper.toAppAction(Strings.ACTION_LOCATION_CM)
                    )
                })
            }
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }


    override fun createActions(app: Application) {
    }

    override fun updateActionValues(app: Application) {}

    companion object {
        /**
         * Independent closure for handling menu actions
         */
        fun createActions(
            app: Application,
            appContext: AppContext,
            display: Display,
            mapView: MapView,
            dispatcher: DispatcherInterface,
            uiController: UiControllerInterface
        ) {

            createActionsClipboard(app, appContext.storage, display, uiController)
            NominatimReverseController(app, appContext, mapView, uiController).addToDispatcher(
                dispatcher
            )
            createActionsCm(app, appContext.dataDirectory)

        }

        private fun createActionsClipboard(
            app: Application,
            storage: StorageInterface,
            display: Display,
            uiController: UiControllerInterface
        ) {
            val clipboard = ClipboardController(display)
            MenuHelper.setAction(app, Strings.ACTION_LOCATION_PASTE) {
                clipboard.getLatLong {
                    AppLog.i(this, it.toString())
                    uiController.centerInMap(it)
                }
            }

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_COPY) {
                val sgrid = SolidMapGrid(storage, GtkCustomMapView.DEFAULT_KEY)
                val text =
                    sgrid.getCode(uiController.getMapBounding().center.toLatLong()).toString()

                AppLog.i(this, text)
                clipboard.setText(
                    sgrid.getCode(uiController.getMapBounding().center.toLatLong()).toString()
                )
            }
        }

        private fun createActionsCm(app: Application, dataDirectory: SolidDataDirectory) {
            val cmApi = CmApi(dataDirectory)
            MenuHelper.setAction(app, Strings.ACTION_LOCATION_CM) {
                cmApi.startTask(GtkAppContext)
            }

        }
    }
}
