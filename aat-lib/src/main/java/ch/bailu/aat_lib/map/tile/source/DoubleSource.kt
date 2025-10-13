package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.cache.Obj
import org.mapsforge.core.model.Tile
import kotlin.math.max

class DoubleSource(
    private val scontext: ServicesInterface,
    private val sourceA: Source,
    private val sourceB: Source,
    zoom: Int
) :
    Source() {
    private val minZoomA =
        max(zoom.toDouble(), sourceA.minimumZoomLevel.toDouble()).toInt()


    override val name: String
        get() = sourceA.name

    override fun getID(tile: Tile, context: AppContext): String {
        return decide(tile).getID(tile, context)
    }


    private fun decide(tile: Tile): Source {
        val r = arrayOf(sourceB)

        if (isZoomLevelSupportedA(tile)) {
            scontext.insideContext {
                if (scontext.getRenderService().supportsTile(tile)) r[0] = sourceA
            }
        }

        return r[0]
    }

    private fun isZoomLevelSupportedA(tile: Tile): Boolean {
        return (tile.zoomLevel <= sourceA.maximumZoomLevel && tile.zoomLevel >= minZoomA)
    }


    override val minimumZoomLevel: Int
        get() = sourceB.minimumZoomLevel

    override val maximumZoomLevel: Int
        get() = sourceA.maximumZoomLevel

    override val isTransparent: Boolean
        get() = sourceA.isTransparent

    override val alpha: Int
        get() = sourceA.alpha


    override fun getFactory(tile: Tile): Obj.Factory {
        return decide(tile).getFactory(tile)
    }
}
