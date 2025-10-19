package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType

open class GpxDataWrapper : GpxDeltaPointInterface, GpxBigDeltaInterface {
    private var summary: GpxBigDeltaInterface = GpxBigDelta.NULL
    private var point: GpxPointInterface = GpxPoint.NULL

    fun setVisibleTrackSegment(s: GpxBigDeltaInterface) {
        summary = s
    }

    fun setVisibleTrackPoint(p: GpxPointInterface) {
        point = p
    }

    override fun getLatitudeE6(): Int {
        return point.getLatitudeE6()
    }

    override fun getLongitudeE6(): Int {
        return point.getLongitudeE6()
    }

    override fun getSpeed(): Float {
        return summary.getSpeed()
    }

    override fun getDistance(): Float {
        return summary.getDistance()
    }

    override fun getStartTime(): Long {
        return summary.getStartTime()
    }

    override fun getTimeDelta(): Long {
        return summary.getTimeDelta()
    }

    override fun getPause(): Long {
        return summary.getPause()
    }

    override fun getAltitude(): Float {
        return point.getAltitude()
    }

    override fun getLatitude(): Double {
        return point.getLatitude()
    }

    override fun getLongitude(): Double {
        return point.getLongitude()
    }

    override fun getTimeStamp(): Long {
        return point.getTimeStamp()
    }

    override fun getAcceleration(): Float {
        return summary.getAcceleration()
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        return summary.getBoundingBox()
    }

    override fun getEndTime(): Long {
        return summary.getEndTime()
    }

    override fun getType(): GpxType {
        return summary.getType()
    }

    override fun getAttributes(): GpxAttributes {
        return summary.getAttributes()
    }
}
