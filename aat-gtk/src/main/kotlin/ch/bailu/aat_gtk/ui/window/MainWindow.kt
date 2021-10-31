package ch.bailu.aat_gtk.ui.window

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.ui.view.MapMainView
import ch.bailu.aat_gtk.ui.view.PreferencesView
import ch.bailu.aat_gtk.ui.view.TrackerButton
import ch.bailu.aat_gtk.ui.view.menu.AppMenu
import ch.bailu.aat_gtk.ui.view.menu.GtkMenu
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.bridge.Image as BridgeImage

class MainWindow(
    window: ApplicationWindow,
    services: ServicesInterface,
    storage: StorageInterface,
    broadcaster: Broadcaster) : Attachable
{

    private val trackerButton = TrackerButton(services)

    private val dispatcher = Dispatcher()
    //private val mapView = MapMainView(services, storage, dispatcher)
    private val preferences = PreferencesView(storage, window)

    private val menu: AppMenu

    init {
        //window.add(mapView.box)
        window.add(preferences.container)
        menu = AppMenu(window, services)
        window.titlebar = createHeader(GtkMenu(menu).menu)

        setIcon(window)
        window.setDefaultSize(720 / 2, 1440 / 2)
        window.onDestroy {
            App.exit(0)
        }

        window.showAll()

        dispatcher.addSource(CurrentLocationSource(services, broadcaster))
        dispatcher.addSource(TrackerSource(services, broadcaster))
        dispatcher.onResume()

        dispatcher.addTarget(trackerButton, InfoID.ALL)
    }

    private fun setIcon(window: ApplicationWindow) {
        try {
            window.icon = BridgeImage.load(javaClass.getResourceAsStream(GtkAppConfig.icon))
        } catch (e: Exception) {
            AppLog.e(e)
        }

        return
    }

    private fun createHeader(menu: Menu): HeaderBar {
        val header = HeaderBar()

        header.showCloseButton = GTK.TRUE
        header.title = Str(GtkAppConfig.title)

        val menuButton = MenuButton()
        menuButton.add(Image.newFromIconNameImage(Str("open-menu-symbolic"), IconSize.BUTTON))
        menuButton.setPopup(menu)

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