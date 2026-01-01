package ch.bailu.aat_gtk.api

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.api.nominatim.NominatimReverseApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay
import ch.bailu.gtk.gtk.Application
import org.mapsforge.map.view.MapView

class NominatimReverseController(app: Application,
                                  private val appContext: AppContext,
                                  private val mapView: MapView,
                                  private val uiController: UiControllerInterface): TargetInterface {

    private val soverlay = SolidNominatimReverseOverlay(appContext.dataDirectory)
    private val reverseApi = NominatimReverseApi(soverlay)
    private var isUpdated = false


    init {
        MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE) { onQueryAction() }
        MenuHelper.setAction(app, Strings.ACTION_LOCATION_REVERSE_CENTER) {
            uiController.centerInMap(soverlay.getIID())
        }
    }


    private fun onQueryAction() {
        if (!reverseApi.isTaskRunning(appContext.services)) {
            reverseApi.startTask(
                appContext,
                uiController.getMapBounding().center,
                mapView.model.mapViewPosition.zoomLevel.toInt()
            )
            soverlay.setEnabled(true)
            isUpdated = true
        }
    }
    private fun getMessage(attributes: GpxAttributes): String {
        return attributes[Keys.toIndex("name")].ifEmpty {
            attributes[Keys.toIndex("city")].ifEmpty {
                attributes[Keys.toIndex("label")]
            }
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (isUpdated) {
            val firstNode = info.getGpxList().pointList.first
            if (firstNode is GpxPointNode) {
                AppLog.i(this, getMessage(firstNode.getAttributes()))
            }
            isUpdated = false
        }
    }

    fun addToDispatcher(dispatcher: DispatcherInterface) {
        dispatcher.addTarget(this, InfoID.NOMINATIM_REVERSE)
    }
}
