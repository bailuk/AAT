package ch.bailu.aat_gtk.controller

interface OverlayControllerInterface {
    fun setEnabled(enabled: Boolean)
    fun frame()
    fun center()
    fun getName(): String
    fun isEnabled(): Boolean
    fun showInDetail()
}
