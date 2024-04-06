package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.map.layer.gpx.GpxLayer
import ch.bailu.aat_lib.map.layer.gpx.legend.GpxLegendLayer
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerAltitudeWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerDistanceWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerSpeedWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.NullLegendWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.PointAltitudeWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.PointDistanceWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.PointIndexWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.PointNameWalker
import ch.bailu.aat_lib.map.layer.gpx.legend.SegmentIndexWalker
import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidLegend(storage: StorageInterface, key: String) : SolidStaticIndexList(
    storage, key + POSTFIX, arrayOf(
        Res.str().none(),
        Res.str().p_legend_fulldistance() + " / " + Res.str().name(),
        Res.str().distance() + " / " + Res.str().name(),
        Res.str().altitude(),
        Res.str().speed()
    )
) {
    fun createTrackLegendLayer(): GpxLayer {
        if (index == 0) return GpxLegendLayer(SegmentIndexWalker())
        if (index == 1) return GpxLegendLayer(MarkerDistanceWalker(getStorage(), false))
        if (index == 2) return GpxLegendLayer(MarkerDistanceWalker(getStorage(), true))
        return if (index == 3) GpxLegendLayer(MarkerAltitudeWalker(getStorage())) else GpxLegendLayer(
            MarkerSpeedWalker(getStorage())
        )
    }

    fun createWayLegendLayer(): GpxLayer {
        if (index == 0) return GpxLegendLayer(NullLegendWalker())
        if (index == 1) return GpxLegendLayer(PointNameWalker())
        if (index == 2) return GpxLegendLayer(PointNameWalker())
        return if (index == 3) GpxLegendLayer(PointAltitudeWalker(getStorage())) else GpxLegendLayer(
            PointIndexWalker()
        )
    }

    fun createRouteLegendLayer(): GpxLayer {
        if (index == 0) return GpxLegendLayer(NullLegendWalker())
        if (index == 1) return GpxLegendLayer(PointDistanceWalker(getStorage(), false))
        if (index == 2) return GpxLegendLayer(PointDistanceWalker(getStorage(), true))
        return if (index == 3) GpxLegendLayer(PointAltitudeWalker(getStorage())) else GpxLegendLayer(
            PointIndexWalker()
        )
    }

    override fun getIconResource(): String {
        return "dialog_question"
    }

    companion object {
        private const val POSTFIX = "_LEGEND"
    }
}
