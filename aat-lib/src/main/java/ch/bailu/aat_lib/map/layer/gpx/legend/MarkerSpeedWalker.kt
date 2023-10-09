package ch.bailu.aat_lib.map.layer.gpx.legend

import ch.bailu.aat_lib.description.CurrentSpeedDescription
import ch.bailu.aat_lib.description.SpeedDescription
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.preferences.StorageInterface

class MarkerSpeedWalker(storage: StorageInterface?) : LegendWalker() {
    private val description: SpeedDescription
    var speed = 0f
    private var samples = 0

    init {
        description = CurrentSpeedDescription(storage!!)
    }

    override fun doList(track: GpxList): Boolean {
        speed = 0f
        samples = 0
        return super.doList(track)
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        val isLast = marker.next == null
        if (!isLast) {
            legendContext!!.setB(marker.firstNode as GpxPointNode)
            if (!legendContext!!.arePointsTooClose()) {
                if (samples > 0) speed /= samples
                drawLegendFromB()
                legendContext!!.switchNodes()
                speed = 0f
                samples = 0
            }
        }
        speed += marker.getSpeed()
        samples++
        return isLast
    }

    override fun doPoint(point: GpxPointNode) {
        if (point.next == null) {
            legendContext!!.setB(point)
            if (!legendContext!!.arePointsTooClose()) {
                speed = speed / samples
            }
            drawLegendFromB()
        }
    }

    private fun drawLegendFromB() {
        if (legendContext!!.isBVisible) {
            legendContext!!.drawNodeB()
            if (!legendContext!!.arePointsTooClose()) {
                legendContext!!.drawLabelB(description.getSpeedDescription(speed))
            }
        }
    }
}
