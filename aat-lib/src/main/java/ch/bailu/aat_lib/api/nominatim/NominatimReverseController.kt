package ch.bailu.aat_lib.api.nominatim

import ch.bailu.aat_lib.api.ApiController
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay

class NominatimReverseController(private val appContext: AppContext,
                                 private val mapView: MapViewInterface
): TargetInterface, ApiController {

    private val soverlay = SolidNominatimReverseOverlay(appContext.dataDirectory)
    private val reverseApi = NominatimReverseApi(soverlay)
    private var isUpdated = false
    private var boundingBox: BoundingBoxE6 = BoundingBoxE6.NULL_BOX


    override fun onAction() {
        if (!reverseApi.isTaskRunning(appContext.services)) {
            val center = mapView.getMapViewPosition().center

            reverseApi.startTask(
                appContext,
                LatLongE6(center.latitudeE6, center.longitudeE6),
                mapView.getMapViewPosition().zoomLevel.toInt()
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

    fun center() {
        if (boundingBox.hasBounding()) {
            mapView.setCenter(boundingBox.center.toLatLong())
        }
    }
    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (isUpdated) {
            val firstNode = info.getGpxList().pointList.first
            boundingBox = info.getBoundingBox()
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
