package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.api.cm.CmApi
import ch.bailu.aat_lib.api.nominatim.NominatimReverseApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str
import org.mapsforge.map.view.MapView
import kotlin.text.ifEmpty

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
            createActionsNominatimReverse(app, appContext, mapView, uiController, dispatcher)
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

        private fun createActionsNominatimReverse(
            app: Application,
            appContext: AppContext,
            mapView: MapView,
            uiController: UiControllerInterface,
            dispatcher: DispatcherInterface
        ) {
            val soverlay = SolidNominatimReverseOverlay(appContext.dataDirectory)
            val reverseApi = NominatimReverseApi(soverlay)
            var isUpdated = false

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE) {
                if (!reverseApi.isTaskRunning(GtkAppContext.services)) {
                    reverseApi.startTask(
                        GtkAppContext,
                        uiController.getMapBounding().center,
                        mapView.model.mapViewPosition.zoomLevel.toInt()
                    )
                    soverlay.setEnabled(true)
                    isUpdated = true
                }
            }

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE_CENTER) {
                uiController.centerInMap(soverlay.getIID())
            }


            dispatcher.addTarget({ iid: Int, info: GpxInformation ->
                if (isUpdated) {
                    val firstNode = info.getGpxList().pointList.first
                    if (firstNode is GpxPointNode) {
                        AppLog.i(this, getMessage(firstNode.getAttributes()))
                    }
                    isUpdated = false
                }
            }, InfoID.NOMINATIM_REVERSE)
        }

        private fun getMessage(attributes: GpxAttributes): String {
            return attributes[Keys.toIndex("name")].ifEmpty {
                attributes[Keys.toIndex("city")].ifEmpty {
                    attributes[Keys.toIndex("label")]
                }
            }
        }
    }
}
