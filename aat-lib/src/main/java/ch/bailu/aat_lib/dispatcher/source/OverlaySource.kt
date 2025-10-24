package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidOverlayFile
import ch.bailu.aat_lib.preferences.map.overlay.SolidCustomOverlayList

class OverlaySource (context: AppContext, infoID: Int, usageTracker: UsageTrackerInterface)
    : FileSource(context, infoID, usageTracker) {

    private val solidFile = SolidOverlayFile(context.storage, context, infoID)

    private val onPreferencesChanged = OnPreferencesChanged { _, key ->
        if (solidFile.hasKey(key)) {
            initAndUpdateOverlay()
        }
    }
    init {
        initAndUpdateOverlay()
    }

    private fun initAndUpdateOverlay() {
        setFile(solidFile.getValueAsFile())
    }
    override fun onPauseWithService() {
        super.onPauseWithService()
        solidFile.unregister(onPreferencesChanged)
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        solidFile.register(onPreferencesChanged)
        initAndUpdateOverlay()
    }
}

fun DispatcherInterface.addOverlaySources(appContext: AppContext, usageTracker: UsageTrackerInterface) {
    for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
        addSource(OverlaySource(appContext, InfoID.OVERLAY + i, usageTracker))
    }
}
