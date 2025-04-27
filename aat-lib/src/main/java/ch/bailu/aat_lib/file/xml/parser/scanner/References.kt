package ch.bailu.aat_lib.file.xml.parser.scanner

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.LatLongE6

class References {
    var id = 0
    var resolved = 0
    var bounding = BoundingBoxE6()
    private val coordinates = HashMap<Int, LatLongE6>(50)

    fun clear() {
        bounding = BoundingBoxE6()
        id = 0
        resolved = 0
    }

    fun put(id: Int, c: LatLongE6) {
        coordinates[id] = c
    }

    operator fun get(key: Int): LatLongE6? {
        return coordinates[key]
    }
}
