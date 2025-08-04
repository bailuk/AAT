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
        propagate()
    }

    fun createTracker(): UsageTracker {
        val result = UsageTracker()
        result.observe { propagate() }
        usageTrackers.add(result)
        propagate()
        return result
    }

    fun createOverlayUsageTracker(storageInterface: StorageInterface, vararg infoIDs: Int): OverlayUsageTracker {
        val result = OverlayUsageTracker(storageInterface, *infoIDs)
        result.observe { propagate() }
        usageTrackers.add(result)
        propagate()
        return result
    }

    fun createSelectableUsageTracker(): SelectableUsageTracker {
        val result = SelectableUsageTracker()
        result.observe { propagate() }
        usageTrackers.add(result)
        propagate()
        return result
    }

    private fun propagate() {
        observers.forEach { it() }
    }
}
