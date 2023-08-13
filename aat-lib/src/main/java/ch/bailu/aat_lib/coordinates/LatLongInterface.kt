package ch.bailu.aat_lib.coordinates

/**
 * Interface to an object that stores geo location
 */
interface LatLongInterface {
    fun getLatitudeE6(): Int
    fun getLongitudeE6(): Int
    fun getLatitude(): Double
    fun getLongitude(): Double
}
