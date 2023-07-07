package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.description.FF
import ch.bailu.aat_lib.xml.parser.util.DoubleScanner
import ch.bailu.aat_lib.xml.parser.util.Stream
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils
import java.io.IOException
import javax.annotation.Nonnull

class BoundingBoxE6 {
    var latNorthE6 = MIN_LA
        private set
    var lonEastE6 = MIN_LO
        private set
    var latSouthE6 = MAX_LA
        private set
    var lonWestE6 = MAX_LO
        private set

    constructor()
    constructor(n: Int, e: Int, s: Int, w: Int) {
        add(n, e, s, w)
    }

    constructor(la: Int, lo: Int) {
        add(la, lo)
    }

    constructor(b: BoundingBox) {
        latNorthE6 = LatLongE6.toE6(b.maxLatitude)
        latSouthE6 = LatLongE6.toE6(b.minLatitude)
        lonWestE6 = LatLongE6.toE6(b.minLongitude)
        lonEastE6 = LatLongE6.toE6(b.maxLongitude)
    }

    constructor(b: BoundingBoxE6) {
        add(b)
    }

    fun add(bounding: String) {
        val stream = Stream(bounding)
        val parser = DoubleScanner(6)
        try {
            parser.scan(stream)
            val s = parser.int
            parser.scan(stream)
            val n = parser.int
            parser.scan(stream)
            val w = parser.int
            parser.scan(stream)
            val e = parser.int
            add(n, e, s, w)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun add(b: BoundingBoxE6) {
        add(
            b.latNorthE6, b.lonEastE6,
            b.latSouthE6, b.lonWestE6
        )
    }

    fun add(latLong: LatLong) {
        add(latLong.latitudeE6, latLong.longitudeE6)
    }

    fun add(point: LatLongInterface) {
        add(point.getLatitudeE6(), point.getLongitudeE6())
    }

    fun add(la: Int, lo: Int) {
        add(la, lo, la, lo)
    }

    fun add(n: Int, e: Int, s: Int, w: Int) {
        latNorthE6 = maxOf(n, latNorthE6)
        lonEastE6 = maxOf(e, lonEastE6)
        latSouthE6 = minOf(s, latSouthE6)
        lonWestE6 = minOf(w, lonWestE6)
    }

    operator fun contains(p: LatLong): Boolean {
        return contains(p.latitudeE6, p.longitudeE6)
    }

    operator fun contains(p: LatLongInterface): Boolean {
        return contains(p.getLatitudeE6(), p.getLongitudeE6())
    }

    fun contains(la: Int, lo: Int): Boolean {
        return la < latNorthE6 && la > latSouthE6 && lo < lonEastE6 && lo > lonWestE6
    }

    fun containsLongitude(b: BoundingBoxE6): Boolean {
        return containsLongitude(b.lonEastE6) || containsLongitude(b.lonWestE6)
    }

    fun containsLongitude(lo: Int): Boolean {
        return lo > lonWestE6 && lo < lonEastE6
    }

    fun containsLatitude(b: BoundingBoxE6): Boolean {
        return containsLatitude(b.latNorthE6) || containsLatitude(b.latSouthE6)
    }

    fun containsLatitude(la: Int): Boolean {
        return la < latNorthE6 && la > latSouthE6
    }

    fun toBoundingBox(): BoundingBox {
        validate()
        val b = BoundingBoxE6(this)
        b.validate()
        return BoundingBox(
            LatLongE6.toD(minOf(b.latSouthE6, b.latNorthE6)),
            LatLongE6.toD(minOf(b.lonWestE6, b.lonEastE6)),
            LatLongE6.toD(maxOf(b.latSouthE6, b.latNorthE6)),
            LatLongE6.toD(maxOf(b.lonWestE6, b.lonEastE6))
        )
    }

    fun hasBounding(): Boolean {
        return latNorthE6 > latSouthE6 && lonEastE6 > lonWestE6
    }

    @Nonnull
    override fun toString(): String {
        val f = FF.f()
        return f.N2.format((latNorthE6 / 1e6f).toDouble()) + "," +
                f.N2.format((lonWestE6 / 1e6f).toDouble()) + "," +
                f.N2.format((latSouthE6 / 1e6f).toDouble()) + "," +
                f.N2.format((lonEastE6 / 1e6f).toDouble())
    }

    val latitudeSpanE6: Int
        get() = Math.abs(latNorthE6 - latSouthE6)
    val longitudeSpanE6: Int
        get() = Math.abs(lonWestE6 - lonEastE6)
    val center: LatLongE6
        get() = LatLongE6(latSouthE6 + latitudeSpanE6 / 2, lonWestE6 + longitudeSpanE6 / 2)

    private fun validate() {
        latNorthE6 = validate(latNorthE6, MIN_LA, MAX_LA)
        latSouthE6 = validate(latSouthE6, MIN_LA, MAX_LA)
        lonEastE6 = validate(lonEastE6, MIN_LO, MAX_LO)
        lonWestE6 = validate(lonWestE6, MIN_LO, MAX_LO)
    }

    companion object {
        private val MIN_LA = LatLongE6.toE6(LatLongUtils.LATITUDE_MIN)
        private val MAX_LA = LatLongE6.toE6(LatLongUtils.LATITUDE_MAX)
        private val MAX_LO = LatLongE6.toE6(LatLongUtils.LONGITUDE_MAX)
        private val MIN_LO = LatLongE6.toE6(LatLongUtils.LONGITUDE_MIN)
        @JvmField
        val NULL_BOX = BoundingBoxE6(0, 0)
        @JvmStatic
        fun doOverlap(b1: BoundingBoxE6, b2: BoundingBoxE6): Boolean {
            return (b1.containsLatitude(b2) || b2.containsLatitude(b1)) &&
                    (b2.containsLongitude(b1) || b1.containsLongitude(b2))
        }

        private fun validate(toValidate: Int, min: Int, max: Int): Int {
            var result = toValidate
            result = minOf(result, max)
            result = maxOf(result, min)
            return result
        }
    }
}
