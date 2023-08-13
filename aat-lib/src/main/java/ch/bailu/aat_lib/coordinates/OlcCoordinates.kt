package ch.bailu.aat_lib.coordinates

import com.google.openlocationcode.OpenLocationCode
import org.mapsforge.core.model.LatLong
import javax.annotation.Nonnull

class OlcCoordinates : Coordinates {
    /**
     * The Open Location Code (OLC) is a geocode system for identifying
     * an area anywhere on the Earth.[1] It was developed at Google's Zürich
     * engineering office,[2] and released late October 2014.[3] Location
     * codes created by the OLC system are referred to as "plus codes".
     * https://en.wikipedia.org/wiki/Open_Location_Code
     *
     *
     * This is a wrapper around
     * com.google.openlocationcode.OpenLocationCode
     *
     */
    private val olc: OpenLocationCode

    constructor(code: String, reference: LatLong) : this(
        code,
        reference.getLatitude(),
        reference.getLongitude()
    ) {
    }

    constructor(code: String, la: Double, lo: Double) {
        val olc = OpenLocationCode(code)
        if (olc.isShort) {
            this.olc = olc.recover(la, lo)
        } else {
            this.olc = olc
        }
    }

    constructor(code: String) {
        olc = OpenLocationCode(code)
    }

    constructor(c: LatLong) {
        olc = OpenLocationCode(c.getLatitude(), c.getLongitude())
    }

    @Nonnull
    override fun toString(): String {
        return olc.code
    }

    override fun toLatLong(): LatLong {
        val area = olc.decode()
        return LatLong(area.centerLatitude, area.centerLongitude)
    }
}
