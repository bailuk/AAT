package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidCriticalMapOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidDraftOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidFixedOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidNominatimReverseOverlay
import ch.bailu.aat_lib.preferences.map.overlay.SolidPoiOverlay

class FixedOverlaySource(context: AppContext, usageTracker: UsageTrackerInterface, private val overlay: SolidFixedOverlay):
    FileSource(context, overlay.getIID(), usageTracker) {

    private val dataDirectory = context.dataDirectory

    private val onPreferencesChanged = { _: StorageInterface, key: String ->
        if (dataDirectory.hasKey(key)) {
            onPreferencesChanged()
        }
    }

    private fun onPreferencesChanged() {
        setFile(overlay.getValueAsFile())
    }

    override fun onResumeWithService() {
        super.onResumeWithService()
        dataDirectory.register(onPreferencesChanged)
        onPreferencesChanged()
    }

    override fun onPauseWithService() {
        dataDirectory.unregister(onPreferencesChanged)
        super.onPauseWithService()
    }

    companion object {
        fun createPoiSource(context: AppContext, usageTracker: UsageTrackerInterface): FileSource {
            val overlay = SolidPoiOverlay(context.dataDirectory)
            return FixedOverlaySource(context, usageTracker, overlay)
        }

        fun createDraftSource(context: AppContext, usageTracker: UsageTrackerInterface): FileSource {
            val overlay = SolidDraftOverlay(context.dataDirectory)
            return FixedOverlaySource(context, usageTracker, overlay)
        }

        fun createNominatimReverseSource(context: AppContext, usageTracker: UsageTrackerInterface): FileSource {
            val overlay = SolidNominatimReverseOverlay(context.dataDirectory)
            return FixedOverlaySource(context, usageTracker, overlay)
        }

        fun createCmSource(context: AppContext, usageTracker: UsageTrackerInterface): FileSource {
            val overlay = SolidCriticalMapOverlay(context.dataDirectory)
            return FixedOverlaySource(context, usageTracker, overlay)
        }
    }
}
