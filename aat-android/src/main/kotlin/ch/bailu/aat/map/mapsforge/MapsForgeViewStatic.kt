package ch.bailu.aat.map.mapsforge

import android.content.Context
import ch.bailu.aat.map.MapDensity
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayerStackConfigured
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayerStackConfigured.BackgroundOnly

class MapsForgeViewStatic(context: Context, appContext: AppContext) : MapsForgeViewBase(
    appContext, context, MapsForgeViewStatic::class.java.simpleName,
    MapDensity(context)
) {
    init {
        val tiles: MapsForgeTileLayerStackConfigured = BackgroundOnly(this, appContext)
        add(tiles)
        isClickable = false
    }
}
