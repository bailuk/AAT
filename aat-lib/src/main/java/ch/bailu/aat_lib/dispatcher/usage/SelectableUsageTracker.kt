package ch.bailu.aat_lib.dispatcher.usage

class SelectableUsageTracker: UsageTrackerInterface {
    private val usageTracker = UsageTracker()
    private var infoID = 0

    override fun observe(onChanged: () -> Unit) {
        usageTracker.observe(onChanged)
    }

    override fun isEnabled(infoID: Int): Boolean {
        return usageTracker.isEnabled(infoID)
    }

    fun select(infoID: Int) {
        this.infoID = infoID
        usageTracker.disableAll()
        usageTracker.setEnabled(infoID, true)
    }

    fun getIID(): Int {
        return infoID
    }
}
