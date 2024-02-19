package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.gpx.GpxLayer
import ch.bailu.aat_lib.map.layer.gpx.RouteLayer
import ch.bailu.aat_lib.map.layer.gpx.TrackLayer
import ch.bailu.aat_lib.map.layer.gpx.TrackOverlayLayer
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

    fun createTrackLayer(mcontext: MapContext): GpxLayer {
        if (index == 0) {
            return TrackLayer(mcontext)
        } else if (index == 1) {
            return RouteLayer(mcontext)
        } else {
            return TrackOverlayLayer(mcontext)
        }
    }

    companion object {
        private const val KEY = "OVERLAY_TYPE"
        private val VAL = arrayOf(
            ToDo.translate("Track"),
            ToDo.translate("Route"),
            ToDo.translate("Track with color")
        )
    }
}
