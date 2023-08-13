package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong

abstract class Coordinates {
    abstract fun toLatLong(): LatLong
}
