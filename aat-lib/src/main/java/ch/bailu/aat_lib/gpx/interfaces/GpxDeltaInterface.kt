package ch.bailu.aat_lib.gpx.interfaces

import ch.bailu.aat_lib.coordinates.BoundingBoxE6

interface GpxDeltaInterface {
    fun getDistance(): Float
    fun getSpeed(): Float
    fun getAcceleration(): Float
    fun getTimeDelta(): Long
    fun getBoundingBox(): BoundingBoxE6
}
