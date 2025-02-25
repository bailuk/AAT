package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.foc.Foc

class MockLocationInformation(file: Foc, state: Int, node: GpxPointNode) : LocationInformation() {
    private val time = System.currentTimeMillis()
    private val node: GpxPointNode

    private val file: Foc
    private val state: Int

    init {
        setVisibleTrackPoint(node)
        this.node = node
        this.file = file
        this.state = state
    }

    override fun getState(): Int {
        return state
    }

    override fun getFile(): Foc {
        return file
    }

    override fun getTimeStamp(): Long {
        return time
    }

    override fun getDistance(): Float {
        return node.getDistance()
    }

    override fun getSpeed(): Float {
        return node.getSpeed()
    }

    override fun getAcceleration(): Float {
        return node.getAcceleration()
    }

    override fun getTimeDelta(): Long {
        return node.getTimeDelta()
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        return node.getBoundingBox()
    }

    override fun hasAccuracy(): Boolean {
        return true
    }

    override fun hasSpeed(): Boolean {
        return true
    }

    override fun hasAltitude(): Boolean {
        return true
    }

    override fun hasBearing(): Boolean {
        return true
    }

    override fun isFromGPS(): Boolean {
        return true
    }

    override fun getCreationTime(): Long {
        return getTimeStamp()
    }

    override fun setAltitude(altitude: Double) {
    }

    override fun getAccuracy(): Float {
        return 5f
    }
}
