package ch.bailu.aat_lib.api.nominatim

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.util.Limit
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

class NominatimReverseApi : Api() {
    override val apiName: String = "Nominatim Reverse"
    override var resultFile: Foc = FocName(apiName)
        private set

    fun startTask(appContext: AppContext, latLong: LatLongInterface, zoom: Int) {
        appContext.services.insideContext {
            val background = appContext.services.getBackgroundService()
            val zoom = Limit.clamp(zoom, 3, 18)
            val url = "https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=${latLong.getLatitude()}&lon=${latLong.getLongitude()}&zoom=${zoom}"

            resultFile = getTarget(appContext)
            val task = DownloadTask(url, resultFile, appContext.downloadConfig)

            background.process(task)
        }
    }

    private fun getTarget(appContext: AppContext): Foc {
        val dir = AppDirectory.getDataDirectory(
            appContext.dataDirectory,
            AppDirectory.DIR_QUERY)
        dir.mkdirs()
        return dir.child("reverse.json")
    }
}