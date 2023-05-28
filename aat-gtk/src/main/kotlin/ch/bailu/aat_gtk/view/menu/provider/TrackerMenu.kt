package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.view.TrackerButtonStartStop
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application

class TrackerMenu (
    private val services: ServicesInterface,
    private val dispatcher: Dispatcher) :
MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSection(Res.str().tracker(), Menu().apply {
                appendItem(MenuHelper.createCustomItem("tracker-button"))
            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = TrackerButtonStartStop(services)
        dispatcher.addTarget(trackerButton, InfoID.TRACKER)

        return arrayOf(CustomWidget(trackerButton.button, "tracker-button"))
    }

    override fun createActions(app: Application) {}
}
