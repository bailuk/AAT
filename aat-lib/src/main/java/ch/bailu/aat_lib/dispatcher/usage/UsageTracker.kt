package ch.bailu.aat_lib.dispatcher.usage

import ch.bailu.aat_lib.lib.adroid_compat.getOrDefaultApi22

class UsageTracker : UsageTrackerInterface {

    private val usageMap = HashMap<Int, Boolean>()
    private val observers = ArrayList<()->Unit>()


    fun setEnabled(infoID: Int, enabled: Boolean) {
        if (enabled != usageMap.getOrDefaultApi22(infoID, false)) {
            usageMap[infoID] = enabled
            observers.forEach { it() }
        }
    }

    override fun isEnabled(infoID: Int): Boolean {
        return usageMap.getOrDefaultApi22(infoID, false)
    }

    override fun observe(onChanged: ()->Unit) {
        observers.add(onChanged)
    }

    fun disableAll() {
        usageMap.keys.forEach {
            setEnabled(it, false)
        }
    }
}
