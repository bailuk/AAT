package ch.bailu.aat_gtk.app.window

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gdkpixbuf.Pixbuf
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory
import org.mapsforge.map.gtk.util.TileCacheUtil
import org.mapsforge.map.gtk.view.MapView
import org.mapsforge.map.layer.download.TileDownloadLayer
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik
import kotlin.system.exitProcess

class MainWindow(app: Application) {

    val title = "AAT"
    val icon = "/images/icon.svg"

    init {
        val window = ApplicationWindow(app)
        val mapView = MapView()
        window.add(mapView.drawingArea)
        window.titlebar = createHeader()

        try {
            window.icon = loadIcon(icon)
        } catch (e: Exception) {
            AppLog.e(e)
        }

        window.setSizeRequest(720 / 2, 1440 / 2)


        window.onShow {
            OpenStreetMapMapnik.INSTANCE.userAgent = "mapsforge-samples-gtk"

            val tileCache = TileCacheUtil.createTileCache(mapView.model)
            val tileDownloadLayer = TileDownloadLayer(
                tileCache,
                mapView.model.mapViewPosition,
                OpenStreetMapMapnik.INSTANCE,
                GtkGraphicFactory.INSTANCE
            )

            mapView.layerManager.layers.add(tileDownloadLayer)
            tileDownloadLayer.start()

            mapView.setZoomLevelMin(OpenStreetMapMapnik.INSTANCE.zoomLevelMin)
            mapView.setZoomLevelMax(OpenStreetMapMapnik.INSTANCE.zoomLevelMax)

            mapView.setZoomLevel(14)
            mapView.model.mapViewPosition.center = LatLong(47.35, 7.9)

        }

        window.onDestroy {
            exitProcess(0)
        }

        window.showAll()
    }

    private fun loadIcon(icon: String): Pixbuf {
        return ch.bailu.gtk.bridge.Image.load(javaClass.getResourceAsStream("/images/icon.svg"))
    }

    private fun createHeader(): HeaderBar {
        val header = HeaderBar()
        header.showCloseButton = GTK.TRUE
        header.title = Str(title)

        val menuButton = MenuButton()
        menuButton.add(Image.newFromIconNameImage(Str("open-menu-symbolic"), IconSize.BUTTON))
        header.packStart(menuButton)

        return header
    }
}