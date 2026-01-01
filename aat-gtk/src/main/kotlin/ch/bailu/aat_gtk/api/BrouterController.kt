package ch.bailu.aat_gtk.api

import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_lib.api.brouter.BrouterApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.information.GpxInformationProvider
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.map.overlay.SolidBrouterOverlay
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

class BrouterController(app: Application, private val appContext: AppContext, private val gpxInformation: GpxInformationProvider, private val uiController: UiControllerInterface) {
    val api = BrouterApi(SolidBrouterOverlay(appContext.dataDirectory))

    init {
        setAction(app, "brouter", { this.onAction() })
    }

    private fun onAction() {
        val list = gpxInformation.getInfo().getGpxList()
        if (validateList(list) && !api.isTaskRunning(appContext.services)) {
            uiController.setOverlayEnabled(InfoID.BROUTER, true)
            api.startTask(appContext, list)
        }
    }

    private fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, action).apply {
            disconnectSignals()
            onActivate(onActivate)
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
