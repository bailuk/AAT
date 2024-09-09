package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.foc.Foc
import org.mapsforge.core.model.Tile

class ObjTileDownloadable(id: String, sc: AppContext, t: Tile, private val source: DownloadSource) : ObjTileCacheOnly(
    id, sc, t, source) {

    override fun onInsert(sc: AppContext) {
        if (isLoadable) {
            load(sc.services)
        } else if (isDownloadable && !isScheduled(sc.services) && !fileExists()) {
            download(sc)
        }
    }

    private fun download(sc: AppContext) {
        val url = source.getTileURLString(getTile())
        sc.services.getBackgroundService().process(FileDownloader(url, file, sc))
    }

    private fun isScheduled(sc: ServicesInterface): Boolean {
        return sc.getBackgroundService().findTask(file) != null
    }

    override fun reDownload(sc: AppContext) {
        if (isDownloadable && !isScheduled(sc.services)) {
            file.rm()
            download(sc)
        }
    }

    private val isDownloadable: Boolean
        get() = (source.maximumZoomLevel >= getTile().zoomLevel &&
                source.minimumZoomLevel <= getTile().zoomLevel
                )

    private class FileDownloader(source: String, target: Foc, val appContext: AppContext) :
        DownloadTask(source, target, appContext.downloadConfig) {

        override fun bgOnProcess(sc: AppContext): Long {
            if (isInCache) {
                return super.bgOnProcess(sc)
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
