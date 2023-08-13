package ch.bailu.aat_lib.dispatcher

interface LifeCycleInterface {
    fun onResumeWithService()
    fun onPauseWithService()
    fun onDestroy()
}
