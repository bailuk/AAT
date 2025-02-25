package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.fs.AppDirectory

class FixedOverlaySource(context: AppContext, usageTracker: UsageTrackerInterface, private val directory: String, private val file: String, iid: Int):
    FileSource(context, iid, usageTracker) {

    private val dataDirectory = context.dataDirectory

    private val onPreferencesChanged = { _: StorageInterface, key: String ->
        if (dataDirectory.hasKey(key)) {
            onPreferencesChanged()
        }
    }

    private fun onPreferencesChanged() {
        setFile(dataDirectory.getValueAsFile().descendant(directory).child(file))
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
            return FixedOverlaySource(context, usageTracker, AppDirectory.DIR_POI, AppDirectory.FILE_POI, InfoID.POI)
        }

        fun createDraftSource(context: AppContext, usageTracker: UsageTrackerInterface): FileSource {
            return FixedOverlaySource(context, usageTracker, AppDirectory.DIR_EDIT, AppDirectory.FILE_DRAFT, InfoID.EDITOR_DRAFT)
        }
    }
}
