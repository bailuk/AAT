package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge
import org.mapsforge.core.model.Tile
import org.mapsforge.map.rendertheme.internal.MapsforgeThemes

class MapsForgeSource(private val themeFile: String) : Source() {
    override val name: String = "MF_" + SolidRenderTheme.toThemeName(themeFile)

    override fun getID(t: Tile, x: AppContext): String {
        return genID(t, name)
    }

    override val minimumZoomLevel: Int
        get() = 0

    override val maximumZoomLevel: Int
        get() = 19

    override val isTransparent: Boolean
        get() = false

    override val alpha: Int
        get() = OPAQUE


    override fun getFactory(t: Tile): Obj.Factory {
        return ObjTileMapsForge.Factory(t, themeFile)
    }

    companion object {
        val MAPSFORGE: Source = MapsForgeSource(MapsforgeThemes.DEFAULT.name)

        const val NAME: String = "Offline"
    }
}
