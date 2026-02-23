package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong

/** Base class for coordinate systems that can be converted to WGS84 [LatLong]. */
abstract class Coordinates {
    abstract fun toLatLong(): LatLong
}
