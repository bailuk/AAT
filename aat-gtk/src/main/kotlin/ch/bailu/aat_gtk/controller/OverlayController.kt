package ch.bailu.aat_gtk.controller

interface OverlayController {
    fun setEnabled(enabled: Boolean)
    fun frame()
    fun center()
    fun getName(): String
    fun isEnabled(): Boolean
    fun showInDetail()
}
