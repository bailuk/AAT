package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res


class SolidScaleFactor(storage: StorageInterface) : SolidIndexList(storage, KEY) {
    val scaleFactor: Float
        get() = VALUE_LIST[index]

    
    override fun getLabel(): String {
        return Res.str().p_mapsforge_scale_factor()
    }

    override fun length(): Int {
        return VALUE_LIST.size
    }

    public override fun getValueAsString(index: Int): String {
        val i = validate(index)
        return if (i == 0) toDefaultString(
            VALUE_LIST[i].toString()
        ) else VALUE_LIST[i].toString()
    }

    companion object {
        private const val KEY = "map_scale_factor"
        private val VALUE_LIST = floatArrayOf(
            1.0f,
            1.2f,
            1.4f,
            1.6f,
            1.8f,
            2.0f,
            2.2f,
            2.4f,
            2.6f,
            0.6f,
            0.8f
        )
    }
}
