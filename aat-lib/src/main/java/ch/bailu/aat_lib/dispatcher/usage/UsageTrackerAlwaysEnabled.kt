package ch.bailu.aat_lib.dispatcher.usage

class UsageTrackerAlwaysEnabled: UsageTrackerInterface {
    override fun observe(onChanged: () -> Unit) {}

    override fun isEnabled(infoID: Int): Boolean {
        return true
    }
}
