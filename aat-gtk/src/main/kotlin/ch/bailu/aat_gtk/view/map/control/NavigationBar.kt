package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.view.solid.SolidImageButton
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.util.IndexedMap

class NavigationBar(mcontext: MapContext, storage: StorageInterface) : Bar(Position.BOTTOM),
    OnContentUpdatedInterface{

    private val infoCache = IndexedMap<Int, GpxInformation>()
    private var boundingCycle = 0

    init {
        add("zoom-in-symbolic").onClicked { mcontext.mapView.zoomIn() }
        add("zoom-out-symbolic").onClicked { mcontext.mapView.zoomOut() }
        add(SolidImageButton(SolidPositionLock(storage, mcontext.solidKey)).button)
        add("zoom-fit-best-symbolic").onClicked {
            if (nextInBoundingCycle()) {
                mcontext.mapView.frameBounding(infoCache.getValueAt(boundingCycle)?.boundingBox)
                AppLog.i(infoCache.getValueAt(boundingCycle)?.getFile()?.name)
            }
        }
    }

    private fun nextInBoundingCycle(): Boolean {
        var c: Int = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getValueAt(boundingCycle)
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
