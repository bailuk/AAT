package ch.bailu.aat_gtk.view

import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button

class TrackerButtonStartStop (private val services: ServicesInterface) : TargetInterface {
    val button = Button()
    private var text = services.getTrackerService().getStartStopText()

    init {
        button.onClicked {
            services.getTrackerService().onStartStop()
        }
        button.setLabel(text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.getTrackerService().getStartStopText()

        if (text != newText) {
            text = newText
            button.setLabel(text)
        }
    }
}
