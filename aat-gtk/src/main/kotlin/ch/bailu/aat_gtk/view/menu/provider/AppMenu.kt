package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.MainStackView
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.type.Str

class AppMenu(private val window: ApplicationWindow, private val stack: MainStackView) :
    MenuProvider {
    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .label(Res.str().intro_map()) {stack.showMap()}
            .label("Cockpit") {stack.showCockpit()}
            .label("Tracks & Overlays") {stack.showFiles()}
            .label(Res.str().intro_settings()) {stack.showPreferences()}
            .separator(Res.str().tracker(), MenuModelBuilder().custom("tracker-button"))
            .separator(
                ToDo.translate("Resolution"),
                MenuModelBuilder()
                    .label("PinePhone low res") {window.setDefaultSize(720 / 2, 1440 / 2)}
                    .label("PinePhone hight res") {window.setDefaultSize(720, 1440)}
            )
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = Button.newWithLabelButton(Str(GtkAppContext.services.trackerService.startStopText))
        trackerButton.onClicked {
            GtkAppContext.services.trackerService.onStartStop()
            trackerButton.label = Str(GtkAppContext.services.trackerService.startStopText)
        }
        return arrayOf(CustomWidget(trackerButton, "tracker-button"))
    }
}
