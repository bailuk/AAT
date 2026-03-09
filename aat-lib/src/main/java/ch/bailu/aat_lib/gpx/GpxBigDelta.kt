package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType

/** Accumulates lifetime totals (distance, time, pause, bounding box) for a track or segment. */
class GpxBigDelta(private val attributes: GpxListAttributes) : GpxBigDeltaInterface {
    private var distance = 0f

    private var startTime: Long = 0
    private var endTime: Long = 0
    private var pause: Long = 0

    private var type = GpxType.TRACK

    private var boundingBox: BoundingBoxE6? = null

    fun update(p: GpxPointNode) {
        doUpdate(p)
        attributes.update(p)
    }

    fun updateWithPause(p: GpxPointNode) {
        if (getEndTime() != 0L) {
            val pause = p.getTimeStamp() - getEndTime()
            if (pause > 0) incPause(pause)
        }
        doUpdate(p)
    }

    private fun doUpdate(p: GpxPointNode) {
        setStartTime(p.getTimeStamp())
        setEndTime(p.getTimeStamp())

        incDistance(p.getDistance())
        addBounding(p.getLatitudeE6(), p.getLongitudeE6())
    }

    fun updateWithPause(delta: GpxBigDeltaInterface) {
        setStartTime(delta.getStartTime())

        incPause(delta.getPause())
        incEndTime(delta.getTimeDelta() + delta.getPause())
        incDistance(delta.getDistance())

        addBounding(delta.getBoundingBox())
    }

    private fun setStartTime(timestamp: Long) {
        if (startTime == 0L) {
            startTime = timestamp
            endTime = timestamp
        }
    }

    private fun incEndTime(t: Long) {
        endTime += t
    }

    private fun setEndTime(timestamp: Long) {
        endTime = timestamp
    }


    private fun incPause(p: Long) {
        pause += p
    }

    private fun incDistance(d: Float) {
        distance += d
    }

    private fun addBounding(b: BoundingBoxE6) {
        val boundingBox = boundingBox
        if (boundingBox == null) {
            this.boundingBox = BoundingBoxE6(b)
        } else {
            boundingBox.add(b)
        }
    }

    private fun addBounding(la: Int, lo: Int) {
        val boundingBox = boundingBox
        if (boundingBox == null) {
            this.boundingBox = BoundingBoxE6(la, lo)
        } else {
            boundingBox.add(la, lo)
        }
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        val boundingBox = boundingBox
        if (boundingBox == null) {
            return BoundingBoxE6.NULL_BOX
        }
        return boundingBox
    }

    override fun getSpeed(): Float {
        val average: Float
        val sitime = (getTimeDelta().toFloat()) / 1000f

        if (sitime > 0f) average = distance / sitime
        else average = 0f

        return average
    }

    override fun getDistance(): Float {
        return distance
    }

    override fun getTimeDelta(): Long {
        return (endTime - startTime) - pause
    }

    override fun getPause(): Long {
        return pause
    }

    override fun getStartTime(): Long {
        return startTime
    }

    override fun getAcceleration(): Float {
        return 0f
    }

    override fun getEndTime(): Long {
        return endTime
    }

    fun setType(t: GpxType) {
        type = t
    }

    override fun getType(): GpxType {
        return type
    }

    override fun getAttributes(): GpxAttributes {
        return attributes
    }

    companion object {
        val NULL: GpxBigDelta = GpxBigDelta(GpxListAttributes.NULL)
    }
}
