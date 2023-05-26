package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.menu.provider.AppMenu
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.adw.SplitButton
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class TrackerButtonStartPauseResume(private val services: ServicesInterface, window: Window, dispatcher: Dispatcher, uiController: UiController) : OnContentUpdatedInterface{
    val button = SplitButton()
    private var text = services.trackerService.pauseResumeText

    init {
        val appMenu = AppMenu(window, GtkAppContext.services, dispatcher, uiController)

        button.menuModel = appMenu.createMenu()

        PopoverMenu(button.popover.cast()).apply {
            appMenu.createCustomWidgets().forEach {
                addChild(it.widget, Str(it.id))
            }
        }
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
