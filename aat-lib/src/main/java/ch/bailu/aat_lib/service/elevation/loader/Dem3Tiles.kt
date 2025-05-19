package ch.bailu.aat_lib.service.elevation.loader

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile

class Dem3Tiles {
    private val tiles: Array<Dem3Tile?> = arrayOfNulls(NUM_TILES)

    init {
        for (i in 0 until NUM_TILES) tiles[i] = Dem3Tile()
    }

    val oldestProcessed: Dem3Tile?
        get() {
            var dem3Tile: Dem3Tile? = null
            var stamp = System.currentTimeMillis()

            for (i in 0 until NUM_TILES) {
                val tile = tiles[i]
                if (tile is Dem3Tile) {
                    if (tile.getStatus() == Dem3Status.LOADING) {
                        return null
                    } else if (!tile.isLocked && tile.timeStamp < stamp) {
                        dem3Tile = tile
                        stamp = tile.timeStamp
                    }
                }
            }

            return dem3Tile
        }

    fun get(index: Int): Dem3Tile? {
        if (index < tiles.size) return tiles[index]
        return null
    }

    fun get(coordinates: Dem3Coordinates): Dem3Tile? {
        for (i in 0 until NUM_TILES) {
            if (tiles[i].hashCode() == coordinates.hashCode()) {
                return tiles[i]
            }
        }
        return null
    }


    fun get(id: String): Dem3Tile? {
        for (i in 0 until NUM_TILES) {
            if (id.contains(tiles[i].toString())) {
                return tiles[i]
            }
        }
        return null
    }

    fun have(id: String): Boolean {
        return get(id) != null
    }

    fun have(coordinates: Dem3Coordinates): Boolean {
        return get(coordinates) != null
    }

    companion object {
        private const val NUM_TILES = 1
    }
}
