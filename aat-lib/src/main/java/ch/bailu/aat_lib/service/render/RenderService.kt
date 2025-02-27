package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.map.SolidRendererThreads
import ch.bailu.aat_lib.preferences.map.SolidScaleFactor
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge
import ch.bailu.foc.FocFactory
import org.mapsforge.core.model.Tile

class RenderService(focFactory: FocFactory, private val sdirectory: SolidMapsForgeDirectory) :
    VirtualService(), OnPreferencesChanged, RenderServiceInterface {
    private val stheme = SolidRenderTheme(sdirectory, focFactory)
    private val scaleFactor = SolidScaleFactor(sdirectory.getStorage())

    private val configuration = Configuration()
    private val caches = Caches()


    init {
        sdirectory.getStorage().register(this)
        reconfigureRenderer()
    }


    private fun reconfigureRenderer() {
        SolidRendererThreads.set()

        val themeID = stheme.valueAsThemeID
        val tileCache = caches.get(themeID)

        configuration.reconfigure(
            sdirectory.getValueAsFile(),
            tileCache,
            stheme.valueAsRenderTheme,
            themeID,
            scaleFactor.scaleFactor
        )

        reSchedule(tileCache)
    }

    private fun reSchedule(cache: Cache) {
        try {
            for (tile in cache.tiles) {
                configuration.lockToRenderer(tile)
            }
        } catch (e: ConcurrentModificationException) {
            e(this, e)
        }
    }

    override fun lockToRenderer(objTileMapsForge: ObjTileMapsForge) {
        caches.lockToRenderer(objTileMapsForge)
        configuration.lockToRenderer(objTileMapsForge)
    }


    override fun freeFromRenderer(objTileMapsForge: ObjTileMapsForge) {
        caches.freeFromRenderer(objTileMapsForge)
    }

    override fun close() {
        sdirectory.getStorage().unregister(this)
        configuration.destroy()
    }


    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sdirectory.hasKey(key) || stheme.hasKey(key)) {
            reconfigureRenderer()
        }
    }

    override fun supportsTile(tile: Tile): Boolean {
        return configuration.supportsTile(tile)
    }
}
