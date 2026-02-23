package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.lib.color.AltitudeColorTable

open class GraphPainter(private val plotter: GraphPlotter, md: Int) : GpxListWalker() {
    private var distance = 0f
    private var summaryDistance = 0f
    private val minDistance: Float = (md * Config.SAMPLE_WIDTH_PIXEL).toFloat()

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        incrementSummaryDistance(point.getDistance())
        plotIfDistance(point)
    }

    fun incrementSummaryDistance(distance: Float) {
        summaryDistance += distance
    }

    fun plotIfDistance(point: GpxPointNode) {
        if (summaryDistance >= minDistance) {
            val altitude = point.getAltitude().toInt()

            distance += summaryDistance
            summaryDistance = 0f

            plotter.plotData(
                distance,
                altitude.toFloat(),
                AltitudeColorTable.getColor(altitude)
            )
        }
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return true
    }

    override fun doList(track: GpxList): Boolean {
        return true
    }
}
