package ch.bailu.aat_lib.view.graph

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.gpx.GpxDistanceWindow
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.AutoPause

class DistanceSpeedPainter(
    private val plotter: List<GraphPlotter>,
    private val autoPause: AutoPause,
    private val minDistance: Float,
    private val window: GpxDistanceWindow)
    : GpxListWalker() {

    private var totalDistance = 0f
    private var totalTime: Long = 0
    private var distanceOfSample = 0f
    private var timeOfSample: Long = 0

    override fun doList(track: GpxList): Boolean {
        return true
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        window.forward(point)
        autoPause.update(point)
        increment(point.getDistance(), point.getTimeDelta())
        plotIfDistance()
    }

    fun increment(distance: Float, time: Long) {
        distanceOfSample += distance
        timeOfSample += time
    }


    fun plotIfDistance() {
        if (distanceOfSample >= minDistance) {
            totalTime += timeOfSample
            totalDistance += distanceOfSample

            plotTotalAverage()
            plotAverage()

            timeOfSample = 0
            distanceOfSample = 0f
        }
    }


    private fun plotAverage() {
        if (window.getTimeDelta() > 0) {
            val avg = window.getSpeed()
            plotter[0].plotData(totalDistance, avg, AppColor.HL_ORANGE)
        }
    }


    private fun plotTotalAverage() {
        val timeDelta = totalTime - autoPause.getPauseTime()

        if (timeDelta > 0) {
            val avg = totalDistance / totalTime * 1000
            plotter[1].plotData(totalDistance, avg, AppColor.HL_GREEN)

            val avgAp = totalDistance / timeDelta * 1000
            plotter[2].plotData(totalDistance, avgAp, AppColor.HL_BLUE)
        }
    }


    override fun doSegment(segment: GpxSegmentNode): Boolean {
        return true
    }
}
