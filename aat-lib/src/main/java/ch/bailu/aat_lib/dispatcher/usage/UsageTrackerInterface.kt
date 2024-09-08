package ch.bailu.aat_lib.dispatcher.usage

interface UsageTrackerInterface {
    fun observe(onChanged: ()->Unit)
    fun isEnabled(infoID: Int): Boolean
}
