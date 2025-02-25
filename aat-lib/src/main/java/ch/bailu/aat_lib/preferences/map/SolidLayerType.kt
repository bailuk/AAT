package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.*
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo

class SolidLayerType(storage: StorageInterface) : SolidIndexList(storage, KEY) {

    override fun length(): Int {
        return VAL.size
    }

    override fun getValueAsString(index: Int): String {
        return VAL[index]
    }

    override fun getLabel(): String {
        return ToDo.translate("Track overlay type")
    }

    fun createTrackLayer(mcontext: MapContext, iid: Int): GpxLayer {
        if (index == 0 || iid < InfoID.OVERLAY) {
            return TrackLayer(mcontext)
        } else if (index == 1) {
            return RouteLayer(mcontext)
        } else if (index == 2) {
            return TrackOverlayLayer(mcontext)
        } else {
            return TrackOverlayLayerShadow(mcontext)
        }
    }

    companion object {
        private const val KEY = "OVERLAY_TYPE"
        private val VAL = arrayOf(
            ToDo.translate("Track"),
            ToDo.translate("Route"),
            ToDo.translate("Track with color"),
            ToDo.translate("Track with color and shadow")
        )
    }
}
