package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.cache.Span
import ch.bailu.aat_lib.util.IndexedMap


class SubTiles {
    private val subTiles = IndexedMap<Dem3Coordinates, SubTile>()
    private var coordinates: Array<Dem3Coordinates?>? = null
    private var inUse = 0

    @Synchronized
    fun haveID(id: String): Boolean {
        for (i in 0 until subTiles.size()) {
            val subTile = subTiles.getValueAt(i)
            if (subTile != null && id.contains(subTile.toString())) {
                return true
            }
        }
        return false
    }

    @get:Synchronized
    val isNotPainting: Boolean
        get() = inUse == 0

    @Synchronized
    fun areAllPainted(): Boolean {
        return isNotPainting && subTiles.size() == 0
    }

    @Synchronized
    fun toSrtmCoordinates(): Array<Dem3Coordinates?> {
        if (coordinates == null || coordinates!!.size != subTiles.size()) {
            coordinates = arrayOfNulls(subTiles.size())

            for (i in 0 until subTiles.size()) {
                coordinates!![i] = subTiles.getValueAt(i)!!.coordinates
            }
        }
        return coordinates!!
    }


    @Synchronized
    fun generateSubTileList(laSpan: ArrayList<Span>, loSpan: ArrayList<Span>) {
        for (la in laSpan.indices) {
            for (lo in loSpan.indices) {
                put(laSpan[la], loSpan[lo])
            }
        }
    }


    private fun put(la: Span, lo: Span) {
        val subTile = SubTile(la, lo)
        subTiles.put(subTile.coordinates, subTile)
    }


    @Synchronized
    fun take(coordinates: Dem3Coordinates): SubTile? {
        val r = subTiles.getValue(coordinates)

        if (r != null) {
            inUse++
            subTiles.remove(coordinates)
        }
        return r
    }


    @Synchronized
    fun done() {
        inUse--
    }
}
