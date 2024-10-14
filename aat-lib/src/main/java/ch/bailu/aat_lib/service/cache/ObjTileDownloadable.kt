package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.foc.Foc
import org.mapsforge.core.model.Tile

class ObjTileDownloadable(id: String, sc: AppContext, t: Tile, private val source: DownloadSource) : ObjTileCacheOnly(
    id, sc, t, source) {

    override fun onInsert(appContext: AppContext) {
        if (isLoadable) {
            load(appContext.services)
        } else if (isDownloadable && !isScheduled(appContext.services) && !fileExists()) {
            download(appContext)
        }
    }

    private fun download(sc: AppContext) {
        val url = source.getTileURLString(getTile())
        sc.services.getBackgroundService().process(FileDownloader(url, getFile(), sc))
    }

    private fun isScheduled(sc: ServicesInterface): Boolean {
        return sc.getBackgroundService().findTask(getFile()) != null
    }

    override fun reDownload(sc: AppContext) {
        if (isDownloadable && !isScheduled(sc.services)) {
            getFile().rm()
            download(sc)
        }
    }

    private val isDownloadable: Boolean
        get() = (source.maximumZoomLevel >= getTile().zoomLevel &&
                source.minimumZoomLevel <= getTile().zoomLevel
                )

    private class FileDownloader(source: String, target: Foc, val appContext: AppContext) :
        DownloadTask(source, target, appContext.downloadConfig) {

        override fun bgOnProcess(appContext: AppContext): Long {
            if (isInCache) {
                return super.bgOnProcess(appContext)
            }
            return 0
        }

        private val isInCache: Boolean
            get() {
                val result = booleanArrayOf(false)

                object : OnObject(appContext, getFile().toString(), ObjTileCacheOnly::class.java) {
                    override fun run(handle: Obj) {
                        result[0] = true
                    }
                }
                return result[0]
            }
    }


    class Factory(private val tile: Tile, private val source: DownloadSource) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjTileDownloadable(id, appContext, tile, source)
        }
    }
}
