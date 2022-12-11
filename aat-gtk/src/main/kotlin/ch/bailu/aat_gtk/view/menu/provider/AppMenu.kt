package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.view.MainStackView
import ch.bailu.aat_gtk.view.TrackerButtonStartStop
import ch.bailu.aat_gtk.view.dialog.About
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.lib.handler.CallbackHandler
import ch.bailu.gtk.lib.handler.SignalHandler
import ch.bailu.gtk.lib.handler.action.ActionHandler
import ch.bailu.gtk.type.Str

class AppMenu(private val window: ApplicationWindow,
              private val services: ServicesInterface,
              private val dispatcher: Dispatcher,
              private val stack: MainStackView) :
    MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {

            append(Res.str().intro_map(), "app.showMap")
            append(Res.str().intro_list(), "app.showTracks")
            append(ToDo.translate("Detail"), "app.trackInfo")
            append(ToDo.translate("Cockpit"), "app.showCockpit")
            appendSection(Res.str().tracker(), Menu().apply {
                appendItem(MenuHelper.createCustomItem("tracker-button"))
            })
            appendSection(Str.NULL, Menu().apply {
                append(Res.str().intro_settings(), "app.showSettings")
                append("${Res.str().intro_about()}…", "app.showAbout")
                append(ToDo.translate("Dump resources"), "app.dumpResources")
            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        val trackerButton = TrackerButtonStartStop(services)
        dispatcher.addTarget(trackerButton, InfoID.TRACKER)

        return arrayOf(CustomWidget(trackerButton.button, "tracker-button"))
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "trackInfo") { stack.showDetail() }
        MenuHelper.setAction(app, "showMap") { stack.showMap() }
        MenuHelper.setAction(app, "showCockpit") { stack.showCockpit() }
        MenuHelper.setAction(app, "showTracks") { stack.showFiles() }
        MenuHelper.setAction(app, "showSettings") { stack.showPreferences() }
        MenuHelper.setAction(app, "showAbout") { About.show(window) }
        MenuHelper.setAction(app, "dumpResources") {
            CallbackHandler.dump(System.out)
            SignalHandler.dump(System.out)
            ActionHandler.dump(System.out)
        }
    }
}
