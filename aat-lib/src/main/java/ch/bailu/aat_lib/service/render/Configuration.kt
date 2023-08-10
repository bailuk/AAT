package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge
import ch.bailu.foc.Foc
import org.mapsforge.core.model.Tile
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.rendertheme.XmlRenderTheme

class Configuration {
    private val mapFiles = ArrayList<Foc>()
    private var renderer: Renderer? = null
    private var themeID: String? = null
    val isConfigured: Boolean
        get() = renderer != null

    fun configure(
        mapDir: Foc,
        cache: TileCache?,
        theme: XmlRenderTheme?,
        tID: String?,
        scaleFactor: Float
    ) {
        if (!isConfigured && configureMapList(mapDir)) {
            themeID = tID
            renderer = try {
                Renderer(theme, cache, mapFiles, scaleFactor)
            } catch (e: Exception) {
                e(renderer, e)
                null
            }
        } else {
            e(this, Res.str().error_no_map_file() + mapDir.path)
        }
    }

    fun destroy() {
        if (isConfigured) {
            renderer!!.destroy()
            renderer = null
        }
    }

    private fun configureMapList(dir: Foc): Boolean {
        mapFiles.clear()
        if (dir.isFile) {
            mapFiles.add(dir)
        } else {
            dir.foreachFile { child: Foc ->
                if (child.name.endsWith(SolidMapsForgeDirectory.EXTENSION)) {
                    mapFiles.add(child)
                }
            }
        }
        return mapFiles.size > 0
    }

    fun supportsTile(t: Tile?): Boolean {
        return isConfigured && renderer!!.supportsTile(t)
    }

    fun lockToRenderer(o: ObjTileMapsForge) {
        if (isConfigured && themeID == o.themeID) {
            renderer!!.addJob(o.tile)
        }
    }
}
