package ch.bailu.aat_lib.api.nominatim

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.Limit

class NominatimReverseApi(overlay: SolidOverlayInterface) : Api(overlay) {
    //override val apiName: String = "Nominatim Reverse"

    fun startTask(appContext: AppContext, latLong: LatLongInterface, zoom: Int) {
        appContext.services.insideContext {
            val background = appContext.services.getBackgroundService()
            val zoom = Limit.clamp(zoom, 3, 18)
            val url = "https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=${latLong.getLatitude()}&lon=${latLong.getLongitude()}&zoom=${zoom}"

            val task = DownloadTask(url, resultFile, appContext.downloadConfig)

            background.process(task)
        }
    }

//        return dir.child("reverse.json")
}