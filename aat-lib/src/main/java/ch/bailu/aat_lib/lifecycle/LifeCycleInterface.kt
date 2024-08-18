package ch.bailu.aat_lib.lifecycle

interface LifeCycleInterface {
    fun onResumeWithService()
    fun onPauseWithService()
    fun onDestroy()
}
