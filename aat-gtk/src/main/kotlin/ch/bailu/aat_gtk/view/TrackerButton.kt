package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.util.UiThread
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.type.Str

class TrackerButton(private val services: ServicesInterface) : OnContentUpdatedInterface{
    val button = Button()
    private var text = services.trackerService.pauseResumeText

    init {
        button.onClicked {
            services.trackerService.onStartPauseResume()
        }
        button.label = Str(text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.trackerService.pauseResumeText

        if (text != newText) {
            UiThread.toUi {
                setNewText(newText)
            }
        }
    }

    private fun setNewText(newText: String) {
        text = newText
        val old = button.label
        button.label = Str(text)
        old.destroy()
    }
}