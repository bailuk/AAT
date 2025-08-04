package ch.bailu.aat_lib.service.elevation.tile

import kotlin.math.abs

/**
 * Translate GeoLocations (latitude or longitude) to the correct index
 * of a DEM Tile
 */
class DemGeoToIndex (private val dim: DemDimension, hasDoubleOffset: Boolean = false) {
    private val offset : Int
    private val lastIndex : Int

    init {
        if (hasDoubleOffset) {
            offset = dim.offset
            lastIndex = dim.dimension - 1 - (dim.offset * 2)
        } else {
            offset = 0
            lastIndex = dim.dimension - 1 - dim.offset
        }
    }

    fun toIndex(laE6: Int, loE6: Int): Int {
        val x = toXIndex(loE6)
        val y = toYIndex(laE6)

        return (y * dim.dimension + x)
    }

    fun toXIndex(loE6: Int): Int {
        if (loE6 < 0) return dim.dimension + inverse(toIndex(loE6))
        return offset + toIndex(loE6)
    }

    fun toYIndex(laE6: Int): Int {
        // Positive value: offset is in first row (index starts with 1)
        if (laE6 > 0) return dim.offset + inverse(toIndex(laE6))

        // Negative value: offset is in last column (index starts with 0)
        return offset + toIndex(laE6)
    }

    /**
     * Scales fraction of decimal coordinate to last index
     * Returns 0-lastIndex
     */
    private fun toIndex(coordinateE6: Int): Int {
        val coordinate : Double = abs(coordinateE6.toDouble()) / 1e6
        val integer: Int = coordinate.toInt()
        val fraction: Double = (coordinate - integer)
        val x = fraction * lastIndex // we use last index!!!
        return Math.round(x).toInt()
    }

    private fun inverse(v: Int): Int {
        return lastIndex - v
    }
}
