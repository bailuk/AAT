package ch.bailu.aat_gtk.view

import ch.bailu.aat_lib.service.tracker.StatusIconInterface

class GtkStatusIcon : StatusIconInterface{
    override fun hide() {}
    override fun showAutoPause() {}
    override fun showPause() {}
    override fun showOn() {}
}