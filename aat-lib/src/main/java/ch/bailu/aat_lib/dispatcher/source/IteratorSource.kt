package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.GpxFileWrapper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxStatic
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.IteratorFollowFile
import ch.bailu.aat_lib.service.directory.IteratorSummary

abstract class IteratorSource(
    private val appContext: AppContext,
    private val iid: Int,
    private val usageTracker: UsageTrackerInterface
) : SourceInterface {

    private val sdirectory: SolidDirectoryQuery =
        SolidDirectoryQuery(appContext.storage, appContext)
    private var iterator = Iterator.NULL
    private var target = TargetInterface.NULL

    private var trackEnabled = usageTracker.isEnabled(iid)

    init {
        usageTracker.observe {
            if (trackEnabled != usageTracker.isEnabled(iid)) {
                trackEnabled = usageTracker.isEnabled(iid)
                if (trackEnabled) {
                    onResumeWithService()
                } else {
                    onPauseWithService()
                }
            }
            requestUpdate()
        }
    }

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    override fun requestUpdate() {
        target.onContentUpdated(iid, getInfo())
    }

    override fun getIID(): Int {
        return iid
    }

    override fun onPauseWithService() {
        iterator.close()
        iterator = Iterator.NULL
    }

    override fun onResumeWithService() {
        if (trackEnabled) {
            iterator = factoryIterator(appContext)
            iterator.moveToPosition(sdirectory.position.getValue())
            iterator.setOnCursorChangedListener({ requestUpdate() })
        }
    }

    override fun onDestroy() {}

    abstract fun factoryIterator(appContext: AppContext): Iterator
    override fun getInfo(): GpxInformation {
        return iterator.getInfo()
    }

    fun moveToPrevious() {
        if (!iterator.moveToPrevious()) iterator.moveToPosition(iterator.getPosition() - 1)
        sdirectory.position.setValue(iterator.getPosition())
        requestUpdate()
    }

    fun moveToNext() {
        if (!iterator.moveToNext()) iterator.moveToPosition(0)
        sdirectory.position.setValue(iterator.getPosition())
        requestUpdate()
    }

    class FollowFile(private val appContext: AppContext) :
        IteratorSource(appContext, InfoID.FILE_VIEW, UsageTrackerAlwaysEnabled()) {
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

    class Summary(appContext: AppContext, usageTracker: UsageTrackerInterface)
        : IteratorSource(appContext, InfoID.LIST_SUMMARY, usageTracker) {
        override fun factoryIterator(appContext: AppContext): Iterator {
            return IteratorSummary(appContext)
        }
    }
}
