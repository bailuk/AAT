package ch.bailu.aat_gtk.view

import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button

class TrackerButtonStartStop (private val services: ServicesInterface) : OnContentUpdatedInterface {
    val button = Button()
    private var text = services.trackerService.getStartStopText()

    init {
        button.onClicked {
            services.trackerService.onStartStop()
        }
        button.setLabel(text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.trackerService.getStartStopText()

        if (text != newText) {
            text = newText
            button.setLabel(text)
        }
    }
}
