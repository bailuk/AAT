package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.cache.gpx.GpxHandler
import ch.bailu.foc.Foc

open class FileSource(
    private val context: AppContext,
    private val iid: Int,
    private val usageCounter: UsageTrackerInterface
)
    : SourceInterface
{
    private val gpxHandler = GpxHandler()
    private var lifeCycleEnabled = false
    private var trackEnabled = true

    private var target = TargetInterface.NULL

    init {
        usageCounter.observe {
            setEnabled(usageCounter.isEnabled(iid))
        }
        setEnabled(usageCounter.isEnabled(iid))
    }
    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    private val onChangedInCache = BroadcastReceiver { args: Array<out String> ->
        if (BroadcastData.has(args, gpxHandler.get().id)) {
            requestUpdate()
        }
    }

    override fun onPauseWithService() {
        lifeCycleEnabled = false
        context.broadcaster.unregister(onChangedInCache)
        gpxHandler.disable()
    }

    override fun onDestroy() {}

    override fun onResumeWithService() {
        lifeCycleEnabled = true
        context.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onChangedInCache)
        if (trackEnabled) {
            gpxHandler.enable(context.services)
        }
    }

    override fun getIID(): Int {
        return iid
    }

    override fun getInfo(): GpxInformation {
        return gpxHandler.info
    }

    open fun setFile(file: Foc) {
        gpxHandler.setFileID(context.services, file)
        requestUpdate()
    }

    private fun setEnabled(enabled: Boolean) {
            trackEnabled = enabled
            if (lifeCycleEnabled && trackEnabled) {
                gpxHandler.enable(context.services)
            } else {
                gpxHandler.disable()
            }
            requestUpdate()
        }

    override fun requestUpdate() {
        context.services.insideContext { target.onContentUpdated(iid, info) }
    }
}
