package ch.bailu.aat_lib.dispatcher.usage

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayFileEnabled

class OverlayUsageTracker(storage: StorageInterface, vararg infoIDs: Int): UsageTrackerInterface {
    private val solids = ArrayList<SolidOverlayFileEnabled>()
    private val usageTracker = UsageTracker()

    init {
        storage.register { _, key ->
            solids.forEach { solid ->
                if (solid.hasKey(key)) {
                    usageTracker.setEnabled(solid.infoID, solid.isEnabled)
                }
            }
        }

        infoIDs.forEach {
            val solid = SolidOverlayFileEnabled(storage, it)
            solids.add(solid)
            usageTracker.setEnabled(it, solid.isEnabled)
        }
    }

    override fun observe(onChanged: () -> Unit) {
        usageTracker.observe(onChanged)
    }

    override fun isEnabled(infoID: Int): Boolean {
        return usageTracker.isEnabled(infoID)
    }
}
