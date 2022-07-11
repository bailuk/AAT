package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkRefs
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.type.Str

class TrackerButtonStartPauseResume(private val services: ServicesInterface) : OnContentUpdatedInterface{
    val button = Button()
    private var text = services.trackerService.pauseResumeText

    init {
        button.onClicked {
            services.trackerService.onStartPauseResume()
        }
        GtkRefs.label(button, text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.trackerService.pauseResumeText

        if (text != newText) {
            text = newText
            GtkRefs.label(button, text)
        }
    }
}
