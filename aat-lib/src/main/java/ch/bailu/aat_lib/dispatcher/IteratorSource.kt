package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.gpx.GpxFileWrapper
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxStatic
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.Iterator.OnCursorChangedListener
import ch.bailu.aat_lib.service.directory.IteratorFollowFile
import ch.bailu.aat_lib.service.directory.IteratorSummary

abstract class IteratorSource(private val appContext: AppContext) : ContentSource(),
    OnCursorChangedListener {
    private val sdirectory: SolidDirectoryQuery = SolidDirectoryQuery(appContext.storage, appContext)
    private var iterator = Iterator.NULL
    override fun onCursorChanged() {
        requestUpdate()
    }

    override fun requestUpdate() {
        sendUpdate(iterator.infoID, info)
    }

    override fun getIID(): Int {
        return iterator.infoID
    }

    override fun onPause() {
        iterator.close()
        iterator = Iterator.NULL
    }

    override fun onResume() {
        iterator = factoryIterator(appContext)
        iterator.moveToPosition(sdirectory.position.getValue())
        iterator.setOnCursorChangedListener(this)
    }

    abstract fun factoryIterator(appContext: AppContext): Iterator
    override fun getInfo(): GpxInformation {
        return iterator.info
    }

    fun moveToPrevious() {
        if (!iterator.moveToPrevious()) iterator.moveToPosition(iterator.count - 1)
        sdirectory.position.setValue(iterator.position)
        requestUpdate()
    }

    fun moveToNext() {
        if (!iterator.moveToNext()) iterator.moveToPosition(0)
        sdirectory.position.setValue(iterator.position)
        requestUpdate()
    }

    class FollowFile(private val appContext: AppContext) : IteratorSource(
        appContext
    ) {
        private var handle = ObjNull.NULL
        override fun factoryIterator(appContext: AppContext): Iterator {
            return IteratorFollowFile(appContext)
        }

        private val onChangedInCache = BroadcastReceiver { objs: Array<out String> ->
            if (iD == objs[0]) {
                requestUpdate()
            }
        }

        override fun onPause() {
            appContext.broadcaster.unregister(onChangedInCache)
            handle.free()
            handle = ObjNull.NULL
            super.onPause()
        }

        override fun onResume() {
            appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onChangedInCache)
            super.onResume()
        }

        override fun getInfo(): GpxInformation {
            val info = arrayOf(super.getInfo())
            appContext.services.insideContext {
                val h = appContext.services.cacheService.getObject(
                    iD,
                    ObjGpxStatic.Factory()
                )
                if (h is ObjGpx) {
                    handle.free()
                    handle = h
                    if (handle.isReadyAndLoaded) info[0] = GpxFileWrapper(
                        handle.file,
                        (handle as ObjGpx).gpxList
                    )
                } else {
                    h.free()
                }
            }
            return info[0]
        }

        private val iD: String
            get() = super.getInfo().file.toString()
    }

    class Summary(appContext: AppContext) : IteratorSource(appContext) {
        override fun factoryIterator(appContext: AppContext): Iterator {
            return IteratorSummary(appContext)
        }
    }
}
