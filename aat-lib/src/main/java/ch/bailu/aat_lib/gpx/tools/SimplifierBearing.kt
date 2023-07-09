package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListWalker
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointFirstNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class SimplifierBearing : GpxListWalker() {
    var newList: GpxList? = null
        private set

    private var newSegment = true
    private var lastBearing = 0.0
    private var currentBearing = 0.0
    override fun doList(track: GpxList): Boolean {
        newList = GpxList(
            track.getDelta().getType(),
            GpxListAttributes.NULL
        )
        return true
    }

    override fun doSegment(segment: GpxSegmentNode): Boolean {
        newSegment = true
        return true
    }

    override fun doMarker(marker: GpxSegmentNode): Boolean {
        return true
    }

    override fun doPoint(point: GpxPointNode) {
        if (newSegment) {
            newSegment = false
            newList?.appendToNewSegment(GpxPoint(point), point.getAttributes())
            if (!isLastInSegment(point)) {
                lastBearing = getBearing(point, point.next as GpxPointInterface).toDouble()
            }
        } else {
            if (isLastInSegment(point) || hasBearingChanged(point)) {
                newList?.appendToCurrentSegment(GpxPoint(point), point.getAttributes())
                lastBearing = currentBearing
            }
        }
    }

    private fun hasBearingChanged(point: GpxPointNode): Boolean {
        currentBearing = getBearing(point, point.next as GpxPointInterface).toDouble()
        val delta = Math.abs(currentBearing - lastBearing)
        return delta >= MIN_BEARING_DELTA
    }

    private fun isLastInSegment(point: GpxPointNode): Boolean {
        return point.next == null || point.next is GpxPointFirstNode
    }

    companion object {
        private const val MIN_BEARING_DELTA = 10.0

        /**
         * Initial bearing (azimuth) from point a to point b
         * https://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/
         * http://www.movable-type.co.uk/scripts/latlong.html
         *
         * @param p1 from this point
         * @param p2 to this point
         * @return bearing in degrees east of true north
         */
        @JvmStatic
        fun getBearing(p1: LatLongInterface, p2: LatLongInterface): Float {
            val la1 = Math.toRadians(p1.getLatitude())
            val la2 = Math.toRadians(p2.getLatitude())
            val lo1 = Math.toRadians(p1.getLongitude())
            val lo2 = Math.toRadians(p2.getLongitude())
            val deltaLo = lo2 - lo1
            val y = cos(la2) * sin(deltaLo)
            val x =
                cos(la1) * sin(la2) - sin(la1) * cos(la2) * cos(deltaLo)
            val radians = atan2(y, x)
            val degrees = Math.toDegrees(radians).toFloat()
            return (degrees + 360f) % 360f
        }
    }
}
