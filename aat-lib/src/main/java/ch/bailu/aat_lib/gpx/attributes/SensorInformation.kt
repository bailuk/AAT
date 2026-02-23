package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.information.GpxInformation

/** Wraps a [GpxAttributes] as a [GpxInformation] snapshot, timestamped at construction time. */
class SensorInformation(private val attributes: GpxAttributes) : GpxInformation() {
    private val timeStamp = System.currentTimeMillis()
    override fun getAttributes(): GpxAttributes {
        return attributes
    }

    override fun getTimeStamp(): Long {
        return timeStamp
    }
}
