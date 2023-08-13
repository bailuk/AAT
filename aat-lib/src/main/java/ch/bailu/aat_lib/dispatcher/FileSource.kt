package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.BroadcastData.has
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.cache.gpx.GpxHandler
import ch.bailu.foc.Foc

open class FileSource(private val context: AppContext, private val iid: Int) : ContentSource(),
    FileSourceInterface {
    private val gpxHandler = GpxHandler()
    private var lifeCycleEnabled = false
    private var trackEnabled = true
    private val onChangedInCache = BroadcastReceiver { args: Array<out String?> ->
        if (has(args, gpxHandler.get().id)) {
            requestUpdate()
        }
    }

    override fun onPause() {
        lifeCycleEnabled = false
        context.broadcaster.unregister(onChangedInCache)
        gpxHandler.disable()
    }

    override fun onResume() {
        lifeCycleEnabled = true
        context.broadcaster.register(onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE)
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

    override fun setFile(file: Foc) {
        gpxHandler.setFileID(context.services, file)
        requestUpdate()
    }


    override fun isEnabled(): Boolean {
        return lifeCycleEnabled && trackEnabled
    }

    override fun setEnabled(enabled: Boolean) {
            trackEnabled = enabled
            if (lifeCycleEnabled && trackEnabled) {
                gpxHandler.enable(context.services)
            } else {
                gpxHandler.disable()
            }
            requestUpdate()
        }


    override fun requestUpdate() {
        context.services.insideContext { sendUpdate(iid, info) }
    }
}
