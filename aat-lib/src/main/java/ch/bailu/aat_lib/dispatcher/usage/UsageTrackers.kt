package ch.bailu.aat_lib.dispatcher.usage

import ch.bailu.aat_lib.preferences.StorageInterface

class UsageTrackers : UsageTrackerInterface {

    private val usageTrackers = ArrayList<UsageTrackerInterface>()
    private val observers = ArrayList<()->Unit>()

    override fun isEnabled(infoID: Int): Boolean {
        for (usageCounter in usageTrackers) {
            if (usageCounter.isEnabled(infoID)) {
                return true
            }
        }
        return false
    }

    override fun observe(onChanged: ()->Unit) {
        observers.add(onChanged)
    }

    fun createTracker(): UsageTrackerInterface {
        return addUsageTracker(UsageTracker())
    }

    fun createOverlayUsageTracker(storageInterface: StorageInterface, vararg infoIDs: Int): UsageTrackerInterface {
        return addUsageTracker(OverlayUsageTracker(storageInterface, *infoIDs))
    }

    fun createSelectableUsageTracker(): SelectableUsageTracker {
        val result = SelectableUsageTracker()
        result.observe { observers.forEach { it() } }
        usageTrackers.add(result)
        return result

    }

    private fun addUsageTracker(usageTracker: UsageTrackerInterface): UsageTrackerInterface {
        usageTracker.observe { observers.forEach { it() } }
        usageTrackers.add(usageTracker)
        return usageTracker
    }


}
