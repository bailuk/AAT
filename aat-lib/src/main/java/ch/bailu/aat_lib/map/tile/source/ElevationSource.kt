package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.elevation.ObjTileElevationColor
import ch.bailu.aat_lib.service.cache.elevation.ObjTileHillShade
import org.mapsforge.core.model.Tile

object ElevationSource {
    @JvmField
    val ELEVATION_HILLSHADE: Source = object : Source() {
        override val name: String = "Hillshade"

        override fun getID(t: Tile, x: AppContext): String {
            return genID(t, name)
        }

        override val minimumZoomLevel: Int
            get() = 8

        override val maximumZoomLevel: Int
            get() = 14

        override val isTransparent: Boolean
            get() = true


        override val alpha: Int
            get() = OPAQUE

        override fun getFactory(mt: Tile): Obj.Factory {
            return ObjTileHillShade.Factory(mt)
        }
    }

    val ELEVATION_COLOR: Source = object : Source() {
        override val name: String
            get() = "ElevationColor"

        override fun getID(t: Tile, x: AppContext): String {
            return genID(t, ObjTileElevationColor::class.java.simpleName)
        }

        override val minimumZoomLevel: Int
            get() = ELEVATION_HILLSHADE.minimumZoomLevel

        override val maximumZoomLevel: Int
            get() = ELEVATION_HILLSHADE.maximumZoomLevel

        override val isTransparent: Boolean
            get() = false

        override val alpha: Int
            get() = 50


        override fun getFactory(mt: Tile): Obj.Factory {
            return ObjTileElevationColor.Factory(mt)
        }
    }
}
