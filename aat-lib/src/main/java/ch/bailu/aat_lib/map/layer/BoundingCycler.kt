package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.util.IndexedMap

class BoundingCycler(private val usageTracker: UsageTrackerInterface): TargetInterface {
    private val infoCache = IndexedMap<Int, GpxInformation>()
    private var boundingCycle = 0

    fun onAction(mcontext: MapContext) {
        val info = nextInBoundingCycle()
        if (info is GpxInformation) {
            val bounding = info.getBoundingBox()
            val fileName = info.getFile().name

            mcontext.getMapView().frameBounding(bounding)
            if (fileName is String) {
                AppLog.i(this, fileName)
            }
        }
    }

    private fun nextInBoundingCycle(): GpxInformation? {
        if (++boundingCycle >= infoCache.size()) {
            boundingCycle = 0
        }
        return infoCache.getValueAt(boundingCycle)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (usageTracker.isEnabled(iid)
            && info.getBoundingBox().hasBounding()
            && info.getGpxList().pointList.size() > 0) {
            infoCache.put(iid, info)
        } else {
            infoCache.remove(iid)
        }
    }
}
