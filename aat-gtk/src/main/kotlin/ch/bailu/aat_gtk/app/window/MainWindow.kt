package ch.bailu.aat_gtk.app.window

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.view.MapMainView
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.bridge.Image as BridgeImage

class MainWindow(
    app: Application,
    services: ServicesInterface,
    storage: StorageInterface,
    broadcaster: Broadcaster) : Attachable {

    private val dispatcher = Dispatcher()
    private val window = ApplicationWindow(app)
    private val mapView = MapMainView(services, storage, dispatcher)

    init {
        window.add(mapView.map.drawingArea)
        window.titlebar = createHeader()

        setIcon(window)
        window.setSizeRequest(720 / 2, 1440 / 2)
        window.onDestroy {
            App.exit(0)
        }

        window.showAll()

        dispatcher.addSource(CurrentLocationSource(services, broadcaster))
        dispatcher.addSource(TrackerSource(services, broadcaster))
        dispatcher.onResume()
    }

    private fun setIcon(window: ApplicationWindow) {
        try {
            window.icon = BridgeImage.load(javaClass.getResourceAsStream(GtkAppConfig.icon))
        } catch (e: Exception) {
            AppLog.e(e)
        }

        return
    }

    private fun createHeader(): HeaderBar {
        val header = HeaderBar()
        header.showCloseButton = GTK.TRUE
        header.title = Str(GtkAppConfig.title)

        val menuButton = MenuButton()
        menuButton.add(Image.newFromIconNameImage(Str("open-menu-symbolic"), IconSize.BUTTON))
        header.packStart(menuButton)

        return header
    }

    override fun onAttached() {
        mapView.onAttached()
    }

    override fun onDetached() {
        mapView.onDetached()
    }


}