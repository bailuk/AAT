package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.MainStackView
import ch.bailu.aat_gtk.view.TrackerButtonStartStop
import ch.bailu.aat_gtk.view.dialog.About
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.ApplicationWindow

class AppMenu(private val window: ApplicationWindow,
              private val services: ServicesInterface,
              private val dispatcher: Dispatcher,
              private val stack: MainStackView) :
    MenuProvider {
    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .label(Res.str().intro_map()) {stack.showMap()}
            .label(ToDo.translate("Cockpit")) {stack.showCockpit()}
            .label(ToDo.translate("Tracks & Overlays")) {stack.showFiles()}
            .label(Res.str().intro_settings()) {stack.showPreferences()}
            .separator(Res.str().tracker(), MenuModelBuilder().custom("tracker-button"))
            .separator(
                "",
                MenuModelBuilder()
                    .label(ToDo.translate("About…")) { About.show(window) }
            )

    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = TrackerButtonStartStop(services)
        dispatcher.addTarget(trackerButton, InfoID.TRACKER)

        return arrayOf(CustomWidget(trackerButton.button, "tracker-button"))
    }
}
