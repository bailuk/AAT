package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.aat_gtk.ui.view.solid.SolidImageButton
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.ActionHelper

class NavigationBar(mcontext: MapContext, storage: StorageInterface) :
    OnContentUpdatedInterface {

    val bar = Bar(Orientation.HORIZONTAL)
    private val plus  = bar.add("zoom-in-symbolic")
    private val minus = bar.add("zoom-out-symbolic")
    private val frame = bar.add("zoom-fit-best-symbolic")


    private val infoCache = IndexedMap<Int, GpxInformation>()

    private var boundingCycle = 0

    init {
        bar.add(SolidImageButton(SolidPositionLock(storage, mcontext.solidKey)).button)

        plus.onClicked { mcontext.mapView.zoomIn() }
        minus.onClicked { mcontext.mapView.zoomOut() }

        frame.onClicked {
            if (nextInBoundingCycle()) {
                mcontext.mapView.frameBounding(infoCache.getAt(boundingCycle)?.boundingBox)
                AppLog.i(infoCache.getAt(boundingCycle)?.getFile()?.name)
            }
        }
    }

    private fun nextInBoundingCycle(): Boolean {
        var c: Int = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getAt(boundingCycle)
            val boundingBox = info?.boundingBox
            val pointList = info?.gpxList?.pointList

            if (boundingBox != null && pointList != null) {
                return boundingBox.hasBounding() && pointList.size() > 0
            }
        }
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.isLoaded) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }
}
