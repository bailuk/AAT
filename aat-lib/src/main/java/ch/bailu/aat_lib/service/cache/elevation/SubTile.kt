package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.cache.Span
import ch.bailu.aat_lib.service.cache.Span.Companion.toRect
import ch.bailu.aat_lib.util.Rect


class SubTile(val laSpan: Span, val loSpan: Span) {
    @JvmField
    val coordinates: Dem3Coordinates =
        Dem3Coordinates(laSpan.deg().toDouble(), loSpan.deg().toDouble())

    fun toRect(): Rect {
        return toRect(laSpan, loSpan)
    }


    fun pixelDim(): Int {
        return loSpan.pixelDim()
    }

    override fun hashCode(): Int {
        return coordinates.hashCode()
    }

    override fun toString(): String {
        return coordinates.toString()
    }
}
