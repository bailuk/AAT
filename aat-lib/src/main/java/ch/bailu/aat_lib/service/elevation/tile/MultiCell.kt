package ch.bailu.aat_lib.service.elevation.tile

abstract class MultiCell {
    abstract fun set(x: Int)
    abstract fun deltaZX(): Int
    abstract fun deltaZY(): Int

    companion object {
        fun factory(dem: DemProvider): MultiCell {
            return if (dem.hasInverseLatitude() && !dem.hasInverseLongitude()) { // NE
                MultiCell4NE(dem)
            } else if (!dem.hasInverseLatitude() && !dem.hasInverseLongitude()) { // SE{
                MultiCell4SE(dem)
            } else if (!dem.hasInverseLatitude() && dem.hasInverseLongitude()) { // SW{
                MultiCell4SW(dem)
            } else { // NW
                MultiCell4NW(dem)
            }
        }
    }
}
