package ch.bailu.aat.api

import ch.bailu.aat_lib.api.nominatim.NominatimReverseApi
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
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay

class NominatimReverseController(private val appContext: AppContext, private val mcontext: MapContext): TargetInterface {
    private val soverlay = SolidNominatimReverseOverlay(appContext.dataDirectory)
    private val reverseApi = NominatimReverseApi(soverlay)
    private var isUpdated = false
    private var boundingBox: BoundingBoxE6 = BoundingBoxE6.NULL_BOX

    fun download() {
        if (!reverseApi.isTaskRunning(appContext.services)) {
            val center = mcontext.getMapView().getMapViewPosition().center
            val zoomLevel = mcontext.getMapView().getMapViewPosition().zoomLevel.toInt()
            reverseApi.startTask(
                appContext,
                LatLongE6(center.latitudeE6, center.longitudeE6),
                zoomLevel
            )
            soverlay.setEnabled(true)
            isUpdated = true
        }
    }

    fun center() {
        if (boundingBox.hasBounding()) {
            mcontext.getMapView().setCenter(boundingBox.center.toLatLong())
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
