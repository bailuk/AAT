package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong
import javax.annotation.Nonnull

abstract class Coordinates {
    @Nonnull
    abstract override fun toString(): String
    abstract fun toLatLong(): LatLong

    companion object {
        @JvmStatic
        fun createIllegalCodeException(code: String): IllegalArgumentException {
            return IllegalArgumentException("The provided code '$code' is not a valid.")
        }
    }
}
