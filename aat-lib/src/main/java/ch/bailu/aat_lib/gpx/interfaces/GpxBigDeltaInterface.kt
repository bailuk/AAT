package ch.bailu.aat_lib.gpx.interfaces

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes

interface GpxBigDeltaInterface : GpxDeltaInterface {
    fun getPause(): Long
    fun getStartTime(): Long
    fun getEndTime(): Long
    fun getType(): GpxType
    fun getAttributes(): GpxAttributes
}
