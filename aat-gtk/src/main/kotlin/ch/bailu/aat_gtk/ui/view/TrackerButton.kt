package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.ui.util.UiThread
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.type.Str

class TrackerButton(private val services: ServicesInterface) : OnContentUpdatedInterface{
    val button = Button()

    init {
        button.onClicked {
            services.trackerService.onStartPauseResume()

        }
        button.label = Str(services.trackerService.pauseResumeText)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (iid == InfoID.TRACKER) {
            UiThread.toUi {
                val old = button.label
                button.label = Str(services.trackerService.pauseResumeText)
                old.destroy()
            }
        }
    }
}