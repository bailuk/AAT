package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.util.IndexedMap

class BoundingCycler: TargetInterface {
    private val infoCache = IndexedMap<Int, GpxInformation>()
    private var boundingCycle = 0

    fun onAction(mcontext: MapContext) {
        if (infoCache.size() > 0 && nextInBoundingCycle()) {
            val info = infoCache.getValueAt(boundingCycle)

            if (info is GpxInformation) {
                val bounding = info.getBoundingBox()
                val fileName = info.getFile().name

                if (fileName != null) {
                    mcontext.getMapView().frameBounding(bounding)
                    AppLog.i(this, fileName)
                }
            }
        }
    }

    private fun nextInBoundingCycle(): Boolean {
        var c = infoCache.size()
        while (c > 0) {
            c--
            boundingCycle++
            if (boundingCycle >= infoCache.size()) boundingCycle = 0

            val info = infoCache.getValueAt(boundingCycle)
            if (info is GpxInformation) {
                if (info.getBoundingBox().hasBounding()
                    && info.getGpxList().pointList.size() > 0
                ) return true
            }
        }
        return false
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getLoaded()) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }
}
