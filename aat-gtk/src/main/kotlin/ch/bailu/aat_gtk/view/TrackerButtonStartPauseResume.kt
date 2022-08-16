package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.lib.extensions.setLabel
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button

class TrackerButtonStartPauseResume(private val services: ServicesInterface) : OnContentUpdatedInterface{
    val button = Button()
    private var text = services.trackerService.pauseResumeText

    init {
        button.onClicked {
            services.trackerService.onStartPauseResume()
        }
        button.setLabel(text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.trackerService.pauseResumeText

        if (text != newText) {
            text = newText
            button.setLabel(text)
        }
    }
}
