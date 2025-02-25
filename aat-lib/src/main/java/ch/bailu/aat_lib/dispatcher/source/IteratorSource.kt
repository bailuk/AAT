package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxFileWrapper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxStatic
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.IteratorFollowFile
import ch.bailu.aat_lib.service.directory.IteratorSummary

abstract class IteratorSource(private val appContext: AppContext) : SourceInterface,
    Iterator.OnCursorChangedListener {
    private val sdirectory: SolidDirectoryQuery =
        SolidDirectoryQuery(appContext.storage, appContext)
    private var iterator = Iterator.NULL
    private var target = TargetInterface.NULL

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    override fun onCursorChanged() {
        requestUpdate()
    }

    override fun requestUpdate() {
        target.onContentUpdated(iterator.infoID, info)
    }

    override fun getIID(): Int {
        return iterator.infoID
    }

    override fun onPauseWithService() {
        iterator.close()
        iterator = Iterator.NULL
    }

    override fun onResumeWithService() {
        iterator = factoryIterator(appContext)
        iterator.moveToPosition(sdirectory.position.getValue())
        iterator.setOnCursorChangedListener(this)
    }

    override fun onDestroy() {}

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
        private var handle: Obj = ObjNull
        override fun factoryIterator(appContext: AppContext): Iterator {
            return IteratorFollowFile(appContext)
        }

        private val onChangedInCache = BroadcastReceiver { objs: Array<out String> ->
            if (iD == objs[0]) {
                requestUpdate()
            }
        }

        override fun onPauseWithService() {
            appContext.broadcaster.unregister(onChangedInCache)
            handle.free()
            handle = ObjNull
            super.onPauseWithService()
        }

        override fun onResumeWithService() {
            appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onChangedInCache)
            super.onResumeWithService()
        }

        override fun getInfo(): GpxInformation {
            val info = arrayOf(super.getInfo())
            appContext.services.insideContext {
                val h = appContext.services.getCacheService().getObject(
                    iD,
                    ObjGpxStatic.Factory()
                )
                if (h is ObjGpx) {
                    handle.free()
                    handle = h
                    if (handle.isReadyAndLoaded()) info[0] = GpxFileWrapper(
                        handle.getFile(),
                        (handle as ObjGpx).getGpxList()
                    )
                } else {
                    h.free()
                }
            }
            return info[0]
        }

        private val iD: String
            get() = super.getInfo().getFile().toString()
    }

    class Summary(appContext: AppContext) : IteratorSource(appContext) {
        override fun factoryIterator(appContext: AppContext): Iterator {
            return IteratorSummary(appContext)
        }
    }
}
