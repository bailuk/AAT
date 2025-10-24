package ch.bailu.aat_lib.api.cm

import ch.bailu.aat_lib.api.Api
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayInterface
import ch.bailu.aat_lib.service.background.DownloadTask

class CmApi(overlay: SolidOverlayInterface) : Api(overlay) {
    private val url = "https://api-gw.criticalmaps.net/locations"

    fun startTask(appContext: AppContext) {
        appContext.services.insideContext {
            val background = appContext.services.getBackgroundService()
            val task = DownloadTask(url, resultFile, appContext.downloadConfig)
            background.process(task)
        }
    }
}
