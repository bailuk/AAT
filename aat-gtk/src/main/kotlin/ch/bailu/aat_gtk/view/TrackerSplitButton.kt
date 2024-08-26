package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.menu.provider.TrackerMenu
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.adw.SplitButton
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

class TrackerSplitButton(private val services: ServicesInterface, dispatcher: Dispatcher) : TargetInterface{
    val button = SplitButton()
    private var text = services.getTrackerService().getPauseResumeText()

    init {
        val menu = TrackerMenu(GtkAppContext.services, dispatcher)

        dispatcher.addTarget(this, InfoID.TRACKER)
        button.menuModel = menu.createMenu()

        PopoverMenu(button.popover.cast()).apply {
            menu.createCustomWidgets().forEach {
                addChild(it.widget, Str(it.id))
            }
        }
        button.onClicked {
            services.getTrackerService().onStartPauseResume()
        }
        button.setLabel(text)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val newText = services.getTrackerService().getPauseResumeText()

        if (text != newText) {
            text = newText
            button.setLabel(text)
        }
    }
}
