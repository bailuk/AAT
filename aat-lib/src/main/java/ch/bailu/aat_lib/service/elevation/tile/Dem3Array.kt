package ch.bailu.aat_lib.service.elevation.tile

import ch.bailu.aat_lib.util.Limit

class Dem3Array {
    val data: ByteArray = ByteArray(DEM3_BUFFER_DIM * DEM3_BUFFER_DIM * 2)
    private val toIndex = DemGeoToIndex(dem3Dimension)

    fun getElevation(laE6: Int, loE6: Int): Short {
        return getElevation(toIndex.toIndex(laE6, loE6))
    }

    fun getElevation(index: Int): Short {
        val index = Limit.clamp(index * 2, 0, data.size-2)
        val x = ((data[index].toInt() shl 8) or (data[index + 1].toInt() and 0xFF)).toShort()
        return x
    }

    companion object {
        private const val DEM3_BUFFER_DIM = 1201
        private const val DEM3_BUFFER_OFFSET = 1
        val dem3Dimension: DemDimension = DemDimension(DEM3_BUFFER_DIM, DEM3_BUFFER_OFFSET)
    }
}
