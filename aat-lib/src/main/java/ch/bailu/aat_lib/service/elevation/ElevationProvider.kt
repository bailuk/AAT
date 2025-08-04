package ch.bailu.aat_lib.service.elevation

interface ElevationProvider {
    fun getElevation(laE6: Int, loE6: Int): Short

    companion object {
        const val NULL_ALTITUDE = 0f
    }
}
