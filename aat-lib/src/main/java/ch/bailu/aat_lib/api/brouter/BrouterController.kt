package ch.bailu.aat_lib.api.brouter

import ch.bailu.aat_lib.api.ApiController
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.information.GpxInformationProvider
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.map.overlay.SolidBrouterOverlay

class BrouterController(private val appContext: AppContext, private val gpxInformation: GpxInformationProvider): ApiController {
    val api = BrouterApi(SolidBrouterOverlay(appContext.dataDirectory))


    override fun onAction() {
        val list = gpxInformation.getInfo().getGpxList()
        if (validateList(list) && !api.isTaskRunning(appContext.services)) {
            api.enableOverlay()
            api.startTask(appContext, list)
        }
    }

    private fun validateList(list: GpxList): Boolean {
        if (list.pointList.size() > 1 && list.pointList.size() <= 20) {
            return true
        }
        AppLog.e(this, "Position count not supported: ${list.pointList.size()}. Must be between 2 and 20")
        return false
    }
}
