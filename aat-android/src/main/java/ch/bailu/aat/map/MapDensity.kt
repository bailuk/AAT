package ch.bailu.aat.map

import android.content.Context
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat_lib.preferences.map.SolidTileSize

class MapDensity : AndroidAppDensity {
    val tileSize: Int

    constructor(context: Context) : super(context) {
        tileSize = SolidTileSize(Storage(context), AndroidAppDensity(context)).tileSize
    }

    constructor() : super(1f, 1f) {
        tileSize = SolidTileSize.DEFAULT_TILESIZE
    }
}
