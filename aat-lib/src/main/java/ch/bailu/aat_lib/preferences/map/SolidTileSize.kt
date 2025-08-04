package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.map.AppDensity
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidTileSize(storage: StorageInterface, density: AppDensity) : SolidIndexList(storage, KEY) {
    private val tileSizeDP: Int = density.toPixelInt(DEFAULT_TILESIZE.toFloat())

    val tileSize: Int
        get() {
            val i = index
            return if (i == 0) {
                tileSizeDP
            } else VALUE_LIST[index]
        }

    override fun getLabel(): String {
        return Res.str().p_tile_size()
    }

    override fun length(): Int {
        return VALUE_LIST.size
    }

    public override fun getValueAsString(index: Int): String {
        val i = validate(index)
        return if (i == 0) toDefaultString(tileSizeDP.toString()) else VALUE_LIST[i].toString()
    }

    companion object {
        private const val KEY = "tile_size"
        const val DEFAULT_TILESIZE = 256
        const val DEFAULT_TILESIZE_BYTES = DEFAULT_TILESIZE * DEFAULT_TILESIZE * 4
        private const val STEP = 32
        private val VALUE_LIST = intArrayOf(
            DEFAULT_TILESIZE + STEP * 8,
            DEFAULT_TILESIZE + STEP * 7,
            DEFAULT_TILESIZE + STEP * 6,
            DEFAULT_TILESIZE + STEP * 5,
            DEFAULT_TILESIZE + STEP * 4,
            DEFAULT_TILESIZE + STEP * 3,
            DEFAULT_TILESIZE + STEP * 2,
            DEFAULT_TILESIZE + STEP * 1,
            DEFAULT_TILESIZE + STEP * 0,
            DEFAULT_TILESIZE + STEP * 8,
            DEFAULT_TILESIZE + STEP * 16,
            DEFAULT_TILESIZE + STEP * 32,
            DEFAULT_TILESIZE + STEP * 64
        )
    }
}
