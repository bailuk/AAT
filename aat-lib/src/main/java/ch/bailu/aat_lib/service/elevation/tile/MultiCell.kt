package ch.bailu.aat_lib.service.elevation.tile

abstract class MultiCell {
    abstract fun set(e: Int)
    abstract fun deltaZX(): Int
    abstract fun deltaZY(): Int

    companion object {
        fun factory(dem: DemProvider): MultiCell {
            return if (dem.inverseLatitude() && !dem.inverseLongitude()) { // NE
                MultiCell4NE(dem)
            } else if (!dem.inverseLatitude() && !dem.inverseLongitude()) { // SE{
                MultiCell4SE(dem)
            } else if (!dem.inverseLatitude() && dem.inverseLongitude()) { // SW{
                MultiCell4SW(dem)
            } else { // NW
                MultiCell4NW(dem)
            }
        }
    }
}
