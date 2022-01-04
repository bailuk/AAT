package ch.bailu.aat_gtk.ui.window

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.ui.view.MainStackView
import ch.bailu.aat_gtk.ui.view.TrackerButton
import ch.bailu.aat_gtk.ui.view.menu.AppMenu
import ch.bailu.aat_gtk.ui.view.menu.GtkMenu
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.HeaderBar
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.helper.ActionHelper
import ch.bailu.gtk.type.Str

class MainWindow(
        actionHelper: ActionHelper,
    window: ApplicationWindow,
    services: ServicesInterface,
    broadcaster: Broadcaster) : Attachable
{

    private val trackerButton = TrackerButton(services)

    private val dispatcher = Dispatcher()
    private val mainView = MainStackView(
            actionHelper,
            services,
            GtkAppContext.storage,
            dispatcher,window)

    private val menu: AppMenu

    init {
        window.child = mainView.layout
        menu = AppMenu(window, services, mainView)
        window.title = Str(GtkAppConfig.title)
        window.titlebar = createHeader(GtkMenu(actionHelper, menu).menu)

        window.setDefaultSize(720 / 2, 1440 / 2)
        window.onDestroy {
            App.exit(0)
        }
        window.show()

        dispatcher.addSource(CurrentLocationSource(services, broadcaster))
        dispatcher.addSource(TrackerSource(services, broadcaster))
        dispatcher.onResume()

        dispatcher.addTarget(trackerButton, InfoID.ALL)
    }


    private fun createHeader(menu: Menu): HeaderBar {
        val header = HeaderBar()

        header.showTitleButtons = GTK.TRUE

        val menuButton = MenuButton()
        menuButton.menuModel = menu

        header.packStart(menuButton)
        header.packStart(trackerButton.button)

        return header
    }

    override fun onAttached() {
        //mapView.onAttached()
    }

    override fun onDetached() {
        //mapView.onDetached()
    }
}
