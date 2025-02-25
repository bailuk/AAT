package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.service.cache.ObjTileMapsForge
import org.mapsforge.core.model.Tile

interface RenderServiceInterface {
    fun lockToRenderer(objTileMapsForge: ObjTileMapsForge)
    fun freeFromRenderer(objTileMapsForge: ObjTileMapsForge)

    fun supportsTile(tile: Tile): Boolean
}
