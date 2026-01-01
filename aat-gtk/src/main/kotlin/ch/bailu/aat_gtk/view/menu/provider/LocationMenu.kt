package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.api.cm.CmApi
import ch.bailu.aat_lib.api.nominatim.NominatimReverseApi
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapGrid
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay
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
            append(Res.str().clipboard_paste(), MenuHelper.toAppAction(Strings.ACTION_LOCATION_PASTE))
            appendSection(ToDo.translate("Reverse Nominatim"), Menu().apply {
                append(ToDo.translate("Update"), MenuHelper.toAppAction(Strings.ACTION_LOCATION_REVERSE))
                append(ToDo.translate("Center"), MenuHelper.toAppAction(Strings.ACTION_LOCATION_REVERSE_CENTER))
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
        fun createActions(storageInterface: StorageInterface,
                          app: Application,
                          display: Display,
                          mapView: MapView,
                          dispatcher: DispatcherInterface,
                          uiController: UiControllerInterface) {
            val clipboard = ClipboardController(display)
            val cmApi = CmApi(GtkAppContext.dataDirectory)
            val soverlay = SolidNominatimReverseOverlay(GtkAppContext.dataDirectory)
            val reverseApi = NominatimReverseApi(soverlay)
            var reverseCenter = false

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

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE) {
                if (!reverseApi.isTaskRunning(GtkAppContext.services)) {
                    reverseApi.startTask(
                        GtkAppContext,
                        uiController.getMapBounding().center,
                        mapView.model.mapViewPosition.zoomLevel.toInt()
                    )
                    soverlay.setEnabled(true)
                    reverseCenter = true
                }
            }

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE_CENTER) {
                uiController.centerInMap(soverlay.getIID())
            }

            MenuHelper.setAction(app, Strings.ACTION_LOCATION_CM) {
                cmApi.startTask(GtkAppContext)
            }

            dispatcher.addTarget({ iid: Int, info: GpxInformation ->
                if (reverseCenter) {
                    val firstNode = info.getGpxList().pointList.first
                    if (firstNode is GpxPointNode) {
                        firstNode.getAttributes().apply {
                            val value = get(Keys.toIndex("label")).ifEmpty {
                                get(Keys.toIndex("name")).ifEmpty {
                                    get(Keys.toIndex("city"))
                                }
                            }
                            AppLog.i(this, value)
                        }
                        uiController.centerInMap(info.getBoundingBox().toBoundingBox().centerPoint)
                    }
                    reverseCenter = false
                }
            }, InfoID.NOMINATIM_REVERSE)
        }
    }
}
