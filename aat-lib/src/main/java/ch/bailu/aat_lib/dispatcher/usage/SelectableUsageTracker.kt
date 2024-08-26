package ch.bailu.aat_lib.dispatcher.usage

class SelectableUsageTracker: UsageTrackerInterface {
    private val usageTracker = UsageTracker()

    override fun observe(onChanged: () -> Unit) {
        usageTracker.observe(onChanged)
    }

    override fun isEnabled(infoID: Int): Boolean {
        return usageTracker.isEnabled(infoID)
    }

    fun select(infoID: Int) {
        usageTracker.disableAll()
        usageTracker.setEnabled(infoID, true)
    }
}
