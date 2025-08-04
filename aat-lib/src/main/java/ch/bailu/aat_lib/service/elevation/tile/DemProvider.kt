package ch.bailu.aat_lib.service.elevation.tile

interface DemProvider {
    /**
     * Elevation at a specific index in the elevation matrix
     */
    fun getElevation(index: Int): Short

    fun getDimension(): DemDimension

    /**
     * Distance in meter across a row/line
     */
    fun getCellDistance(): Float
    fun hasInverseLatitude(): Boolean
    fun hasInverseLongitude(): Boolean

    companion object {
        val NULL: DemProvider = object : DemProvider {
            override fun getElevation(index: Int): Short {
                return 0
            }

            override fun getDimension(): DemDimension {
                return Dem3Array.dem3Dimension
            }

            override fun getCellDistance(): Float {
                return 50f
            }

            override fun hasInverseLatitude(): Boolean {
                return false
            }

            override fun hasInverseLongitude(): Boolean {
                return false
            }
        }
    }
}
