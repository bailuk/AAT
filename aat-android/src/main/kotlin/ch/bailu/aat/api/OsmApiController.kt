package ch.bailu.aat.api

import ch.bailu.aat_lib.api.ApiConfiguration
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6

class OsmApiController(val api: ApiConfiguration,  private val appContext: AppContext, val boundingBox: BoundingBoxE6) {

    fun download() {
        val serviceContext = appContext.services
        api.apply {
            if (isTaskRunning(serviceContext)) {
                stopTask(serviceContext)
            } else {
                startTask(appContext, boundingBox)
                api.enableOverlay()
            }
        }
    }

}
