package ch.bailu.aat_lib.service.tracker

interface StateInterface {
    fun updateTrack()
    fun onStartPauseResume()
    fun onStartStop()
    fun onPauseResume()
    fun getStateID(): Int
    fun getStartStopText(): String
    fun getPauseResumeText(): String
    fun getStartStopIcon(): String
}
