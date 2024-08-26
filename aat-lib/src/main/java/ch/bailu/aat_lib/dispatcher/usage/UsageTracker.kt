package ch.bailu.aat_lib.dispatcher.usage

class UsageTracker : UsageTrackerInterface {

    private val usageMap = HashMap<Int, Boolean>()
    private val observers = ArrayList<()->Unit>()


    fun setEnabled(infoID: Int, enabled: Boolean) {
        if (enabled != usageMap.getOrDefault(infoID, false)) {
            usageMap[infoID] = enabled
            observers.forEach { it() }
        }
    }

    override fun isEnabled(infoID: Int): Boolean {
        val result = usageMap[infoID]

        if (result is Boolean) {
            return result
        }
        return false
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
