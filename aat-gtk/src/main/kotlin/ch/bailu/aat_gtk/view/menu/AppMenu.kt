package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.lib.menu.MenuFacade
import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.MainStackView
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

class AppMenu(private val app: Application, private val window: ApplicationWindow, private val stack: MainStackView) : MenuProvider {
    override fun createMenu(): Menu {
        val menuFacade = MenuFacade(app)

        menuFacade.build()
            .label(Res.str().intro_map()) {stack.showMap()}
            .label("Cockpit") {stack.showCockpit()}
            .label("Tracks & Overlays") {stack.showFiles()}
            .label(Res.str().intro_settings()) {stack.showPreferences()}
            .custom("tracker-button")
            .separator(
                MenuModelBuilder()
                    .label("PinePhone low res") {window.setDefaultSize(720 / 2, 1440 / 2)}
                    .label("PinePhone hight res") {window.setDefaultSize(720, 1440)}
            )

        return menuFacade.model
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = Button.newWithLabelButton(Str(GtkAppContext.services.trackerService.startStopText))
        trackerButton.onClicked {
            GtkAppContext.services.trackerService.onStartStop()
            trackerButton.label = Str(GtkAppContext.services.trackerService.startStopText)
        }
        return arrayOf(CustomWidget("tracker-button", trackerButton))
    }

}

data class CustomWidget(val id: String, val widget: Widget)

interface MenuProvider {
    fun createMenu() : Menu
    fun createCustomWidgets() : Array<CustomWidget>
}
